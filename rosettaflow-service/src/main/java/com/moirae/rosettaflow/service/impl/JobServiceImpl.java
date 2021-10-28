package com.moirae.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.common.constants.SysConstant;
import com.moirae.rosettaflow.common.enums.*;
import com.moirae.rosettaflow.common.exception.BusinessException;
import com.moirae.rosettaflow.common.utils.BeanCopierUtils;
import com.moirae.rosettaflow.dto.JobDto;
import com.moirae.rosettaflow.mapper.JobMapper;
import com.moirae.rosettaflow.mapper.domain.Job;
import com.moirae.rosettaflow.mapper.domain.SubJob;
import com.moirae.rosettaflow.mapper.domain.SubJobNode;
import com.moirae.rosettaflow.mapper.domain.Workflow;
import com.moirae.rosettaflow.service.IJobService;
import com.moirae.rosettaflow.service.ISubJobNodeService;
import com.moirae.rosettaflow.service.ISubJobService;
import com.moirae.rosettaflow.service.IWorkflowService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static cn.hutool.core.date.DateTime.now;

/**
 * @author hudenian
 * @date 2021/8/13
 * @description 作业处理类
 */
@Slf4j
@Service
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements IJobService {

    @Resource
    private IWorkflowService workflowService;

    @Resource
    private ISubJobService subJobService;

    @Resource
    private ISubJobNodeService subJobNodeService;

    @Resource
    private RedissonClient redissonClient;

    @Override
    public List<Job> getAllUnfinishedJob() {
        //包含两部分数据：1-重复执行未超过结束时间的数据，2-只执行一次，运行中的数据
        LambdaQueryWrapper<Job> wrapper = Wrappers.lambdaQuery();
        wrapper.and(w1 -> w1.eq(Job::getRepeatFlag, JobRepeatEnum.REPEAT.getValue())
                        .le(Job::getBeginTime, new Date(System.currentTimeMillis()))
                        .ge(Job::getEndTime, new Date(System.currentTimeMillis()))
                        .eq(Job::getStatus, StatusEnum.VALID.getValue()))
                .or(w2 -> w2.eq(Job::getJobStatus, JobStatusEnum.RUNNING.getValue())
                        .eq(Job::getRepeatFlag, JobRepeatEnum.NOREPEAT.getValue())
                        .eq(Job::getStatus, StatusEnum.VALID.getValue()))
                .orderByAsc(Job::getId);
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public IPage<JobDto> list(Long current, Long size, String jobName) {
        Page<JobDto> jobPage = new Page<>(current, size);
        return this.baseMapper.queryJobList(jobName, jobPage);
    }

    @Override
    public Job getValidJobById(Long id) {
        LambdaQueryWrapper<Job> jobLambdaQueryWrapper = Wrappers.lambdaQuery();
        jobLambdaQueryWrapper.eq(Job::getId, id);
        jobLambdaQueryWrapper.eq(Job::getStatus, StatusEnum.VALID.getValue());
        return this.getOne(jobLambdaQueryWrapper);
    }

    @Override
    public void updateBatchStatus(Object[] ids, Byte status) {
        LambdaUpdateWrapper<Job> updateJobWrapper = Wrappers.lambdaUpdate();
        updateJobWrapper.set(Job::getStatus, status);
        updateJobWrapper.set(Job::getUpdateTime, now());
        updateJobWrapper.in(Job::getId, ids);
        this.update(updateJobWrapper);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void add(JobDto jobDto) {
        checkParam(jobDto);
        //保存作业
        Job job = new Job();
        BeanCopierUtils.copy(jobDto, job);
        job.setJobStatus(JobStatusEnum.UN_START.getValue());

        if (!this.save(job)) {
            log.error("Class:{}->,{}", this.getClass(), ErrorMsg.JOB_ADD_ERROR.getMsg());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.JOB_ADD_ERROR.getMsg());
        }

        //将作业保存至redis待后续处理
        try {
            redissonClient.getBlockingQueue(SysConstant.JOB_ADD_QUEUE).put(job);
        } catch (InterruptedException e) {
            log.error("Job add to redis fail,fail reason:{}", e.getMessage(), e);
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.JOB_ADD_ERROR.getMsg());
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void edit(JobDto jobDto) {
        Job job = this.getValidJobById(jobDto.getId());
        if (null == job) {
            log.error("Class:{}->,{}", this.getClass(), ErrorMsg.JOB_NOT_EXIST.getMsg());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.JOB_NOT_EXIST.getMsg());
        }
        //如果作业正在执行或者执行完成则不能够修改
        if (job.getJobStatus() == JobStatusEnum.RUNNING.getValue() || job.getJobStatus() == JobStatusEnum.FINISH.getValue()) {
            log.error("Class:{}->,{}", this.getClass(), ErrorMsg.JOB_RUNNING_OR_FINISH.getMsg());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.JOB_RUNNING_OR_FINISH.getMsg());
        }

        checkParam(jobDto);
        //修改作业
        BeanCopierUtils.copy(jobDto, job);
        job.setJobStatus(JobStatusEnum.UN_START.getValue());
        job.setStatus(StatusEnum.VALID.getValue());
        if (job.getRepeatFlag() == JobRepeatEnum.NOREPEAT.getValue()) {
            job.setRepeatInterval(null);
            job.setEndTime(null);
            job.setEndTimeFlag(JobEndTimeLimitEnum.LIMIT.getValue());
            job.setStatus(StatusEnum.VALID.getValue());
            job.setUpdateTime(new Date());
        }
        if (this.baseMapper.updateJobById(job) == 0) {
            log.error("Class:{}->,{}", this.getClass(), ErrorMsg.JOB_EDIT_ERROR.getMsg());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.JOB_EDIT_ERROR.getMsg());
        }

        //将作业保存至redis待后续处理
        try {
            redissonClient.getBlockingQueue(SysConstant.JOB_EDIT_QUEUE).put(job);
        } catch (InterruptedException e) {
            log.error("Edit->Job add to redis fail,fail reason:{}", e.getMessage(), e);
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.JOB_EDIT_ERROR.getMsg());
        }
    }

    @Override
    public void editBasicInfo(JobDto jobDto) {
        Job job = this.getValidJobById(jobDto.getId());
        if (null == job) {
            log.error("Job does not exist edit failed, jobId:{}->,{}", jobDto.getId(), ErrorMsg.JOB_NOT_EXIST.getMsg());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.JOB_NOT_EXIST.getMsg());
        }
        //如果作业正在执行不能够修改
        if (job.getJobStatus() == JobStatusEnum.RUNNING.getValue()) {
            log.error("Job is running edit failed, jobId:{}->,{}", jobDto.getId(), ErrorMsg.JOB_NOT_EDIT.getMsg());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.JOB_NOT_EDIT.getMsg());
        }
        LambdaUpdateWrapper<Job> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(Job::getId, jobDto.getId());
        updateWrapper.set(Job::getName, jobDto.getName());
        updateWrapper.set(Job::getDesc, jobDto.getDesc());
        if (!this.update(updateWrapper)) {
            log.error("Class:{}->,{}", this.getClass(), ErrorMsg.JOB_EDIT_ERROR.getMsg());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.JOB_EDIT_ERROR.getMsg());
        }
    }

    @Override
    public List<Workflow> queryRelatedWorkflowName(Long projectId) {
        return workflowService.queryWorkFlowByProjectId(projectId);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void pause(Long id) {
        Job job = this.getById(id);
        if (job.getJobStatus() != JobStatusEnum.RUNNING.getValue()) {
            log.error("job is not running can not modify by jobId:{}", job.getId());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.JOB_NOT_RUNNING.getMsg());
        }

        try {
            redissonClient.getBlockingQueue(SysConstant.JOB_PAUSE_QUEUE).put(job);
        } catch (InterruptedException e) {
            log.error("Pause->Job add to redis fail,fail reason:{}", e.getMessage(), e);
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.JOB_PAUSE_ERROR.getMsg());
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void reStart(Long id) {
        Job job = this.getById(id);
        if (job.getJobStatus() != JobStatusEnum.STOP.getValue()) {
            log.error("job is not stop can not modify by jobId:{}", job.getId());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.JOB_NOT_STOP.getMsg());
        }

        try {
            redissonClient.getBlockingQueue(SysConstant.JOB_ADD_QUEUE).put(job);
        } catch (InterruptedException e) {
            log.error("ReStart->Job add to redis fail,fail reason:{}", e.getMessage(), e);
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.JOB_ADD_ERROR.getMsg());
        }
    }

    @Override
    public List<Job> listRunJobByWorkflowId(Long workflowId) {
        LambdaQueryWrapper<Job> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Job::getWorkflowId, workflowId);
        wrapper.eq(Job::getJobStatus, JobStatusEnum.RUNNING.getValue());
        return this.list(wrapper);
    }

    @Override
    public List<Job> listRunAllJob() {
        LambdaQueryWrapper<Job> jobQueryWrapper = Wrappers.lambdaQuery();
        jobQueryWrapper.eq(Job::getJobStatus, JobStatusEnum.RUNNING.getValue());
        jobQueryWrapper.eq(Job::getStatus, StatusEnum.VALID.getValue());
        return this.list(jobQueryWrapper);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public void deleteBatchJob(List<Long> ids) {
        if (ids.isEmpty()) {
            log.error(ErrorMsg.JOB_ID_NOT_EXIST.getMsg());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.JOB_ID_NOT_EXIST.getMsg());
        }
        List<Job> jobs = this.listByIds(ids);
        if (jobs.isEmpty() || ids.size() != jobs.size()) {
            log.error(ErrorMsg.JOB_NOT_EXIST.getMsg());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.JOB_NOT_EXIST.getMsg());
        }
        //检查作业是否存在运行中
        boolean isExistRunningJob = jobs.stream().anyMatch(job -> job.getJobStatus() == JobStatusEnum.RUNNING.getValue());
        if (isExistRunningJob) {
            log.error(ErrorMsg.JOB_NOT_DELETE.getMsg());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.JOB_NOT_DELETE.getMsg());
        }
        //检查子作业是否存在运行中
        Object[] subJobArrayIds = null;
        List<SubJob> subJobList = subJobService.queryBatchSubJobListByJobId(ids);
        if (!Objects.isNull(subJobList) && !subJobList.isEmpty()) {
            subJobArrayIds = subJobList.stream().map(SubJob::getId).toArray();
            boolean isExistRunningSubJob = subJobList.stream().anyMatch(subJob -> subJob.getSubJobStatus() == SubJobStatusEnum.RUNNING.getValue());
            if (isExistRunningSubJob) {
                log.error(ErrorMsg.SUB_JOB_NOT_DELETE.getMsg());
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.SUB_JOB_NOT_DELETE.getMsg());
            }
        }
        //检查子作业节点是否存在运行中节点
        Object[] subJobNodeArrayIds = null;
        if (!Objects.isNull(subJobArrayIds) && subJobArrayIds.length > 0) {
            List<SubJobNode> subJobNodeList = subJobNodeService.queryBatchSubJobListNodeByJobId(subJobArrayIds);
            subJobNodeArrayIds = subJobNodeList.stream().map(SubJobNode::getId).toArray();
            boolean isExistRunningSubJobNode = subJobNodeList.stream().anyMatch(subJobNode -> subJobNode.getRunStatus() == SubJobNodeStatusEnum.RUNNING.getValue());
            if (isExistRunningSubJobNode) {
                log.error(ErrorMsg.SUB_JOB_NODE_NOT_DELETE.getMsg());
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.SUB_JOB_NODE_NOT_DELETE.getMsg());
            }
        }
        //批量更新作业、子作业、子作业节点有效状态
        if (ids.toArray().length > 0) {
            this.updateBatchStatus(ids.toArray(), StatusEnum.UN_VALID.getValue());
        }
        if (!Objects.isNull(subJobArrayIds) && subJobArrayIds.length > 0) {
            subJobService.updateBatchStatus(subJobArrayIds, StatusEnum.UN_VALID.getValue());
        }
        if (!Objects.isNull(subJobNodeArrayIds) && subJobNodeArrayIds.length > 0) {
            subJobNodeService.updateBatchStatus(subJobNodeArrayIds, StatusEnum.UN_VALID.getValue());
        }
    }

    /**
     * 检查入参合法性
     */
    private void checkParam(JobDto jobDto) {
        //校验开始时间 > 结束时间
        if (!Objects.isNull(jobDto.getBeginTime()) && !Objects.isNull(jobDto.getEndTime()) && jobDto.getBeginTime().after(jobDto.getEndTime())) {
            log.error("Class:{}->,{}", this.getClass(), ErrorMsg.JOB_TIME_ERROR.getMsg());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.JOB_TIME_ERROR.getMsg());
        }
        //作业执行重复时,重复次数合法性
        if (jobDto.getRepeatFlag() == JobRepeatEnum.REPEAT.getValue()) {
            if (Objects.isNull(jobDto.getRepeatInterval())) {
                log.error("Class:{}->,{}", this.getClass(), ErrorMsg.JOB_REPEAT_INTERVAL_ERROR.getMsg());
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.JOB_REPEAT_INTERVAL_ERROR.getMsg());
            }
        } else {
            if (!Objects.isNull(jobDto.getRepeatInterval())) {
                log.error("Class:{}->,{}", this.getClass(), ErrorMsg.JOB_NO_REPEAT_INTERVAL_ERROR.getMsg());
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.JOB_NO_REPEAT_INTERVAL_ERROR.getMsg());
            }
        }
        //作业结束时间校验
        if (jobDto.getEndTimeFlag() == JobEndTimeLimitEnum.LIMIT.getValue()) {
           if (Objects.isNull(jobDto.getEndTime())) {
               log.error("Class:{}->,{}", this.getClass(), ErrorMsg.JOB_ENDTIME_NOT_NULL.getMsg());
               throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.JOB_ENDTIME_NOT_NULL.getMsg());
           }
        } else {
            if (!Objects.isNull(jobDto.getEndTime())) {
                log.error("Class:{}->,{}", this.getClass(), ErrorMsg.JOB_ENDTIME_NULL.getMsg());
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.JOB_ENDTIME_NULL.getMsg());
            }
        }
    }
}
