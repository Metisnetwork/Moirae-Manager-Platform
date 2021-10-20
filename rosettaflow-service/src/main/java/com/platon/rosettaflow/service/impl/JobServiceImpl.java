package com.platon.rosettaflow.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.common.constants.SysConstant;
import com.platon.rosettaflow.common.enums.*;
import com.platon.rosettaflow.common.exception.BusinessException;
import com.platon.rosettaflow.common.utils.BeanCopierUtils;
import com.platon.rosettaflow.dto.JobDto;
import com.platon.rosettaflow.mapper.JobMapper;
import com.platon.rosettaflow.mapper.domain.Job;
import com.platon.rosettaflow.mapper.domain.SubJob;
import com.platon.rosettaflow.mapper.domain.SubJobNode;
import com.platon.rosettaflow.mapper.domain.Workflow;
import com.platon.rosettaflow.service.IJobService;
import com.platon.rosettaflow.service.ISubJobNodeService;
import com.platon.rosettaflow.service.ISubJobService;
import com.platon.rosettaflow.service.IWorkflowService;
import com.zengtengpeng.annotation.MQPublish;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    public List<Job> getAllUnfinishedJob() {
        LambdaQueryWrapper<Job> wrapper = Wrappers.lambdaQuery();
        wrapper.and(jobLambdaQueryWrapper -> jobLambdaQueryWrapper.eq(Job::getJobStatus, JobStatusEnum.UN_START.getValue())
                    .le(Job::getBeginTime, new Date(System.currentTimeMillis()))
                    .ge(Job::getEndTime, new Date(System.currentTimeMillis()))
                    .eq(Job::getStatus, StatusEnum.VALID.getValue()))
               .or(jobLambdaQueryWrapper -> jobLambdaQueryWrapper.eq(Job::getJobStatus, JobStatusEnum.RUNNING.getValue()).eq(Job::getStatus, StatusEnum.VALID.getValue()))
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
        jobLambdaQueryWrapper.eq(Job::getStatus,StatusEnum.VALID.getValue());
        return this.getOne(jobLambdaQueryWrapper);
    }

    @Override
    public void updateBatchStatus(Object[] ids, Byte status) {
        LambdaUpdateWrapper<Job> updateJobWrapper = Wrappers.lambdaUpdate();
        updateJobWrapper.set(Job::getStatus,status);
        updateJobWrapper.set(Job::getUpdateTime,now());
        updateJobWrapper.in(Job::getId, ids);
        this.update(updateJobWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(JobDto jobDto) {
        checkParam(jobDto);
        //保存作业
        Job job = new Job();
        BeanCopierUtils.copy(jobDto, job);
        job.setJobStatus(JobStatusEnum.UN_START.getValue());

        if (!this.save(job)) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.JOB_ADD_ERROR.getMsg());
        }

        //将作业保存至redis待后续处理
        addJobPublish(job);
//        redisUtil.listLeftPush(SysConstant.JOB_ADD_QUEUE, JSON.toJSONString(job), null);
    }

    @Override
    public void edit(JobDto jobDto) {
        Job jobOriginal = new Job();
        Job job = this.getValidJobById(jobDto.getId());
        if (null == job) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.JOB_NOT_EXIST.getMsg());
        }
        //如果作业正在执行或者执行完成则不能够修改
        if (job.getJobStatus() == JobStatusEnum.RUNNING.getValue() || job.getJobStatus() == JobStatusEnum.FINISH.getValue()) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.JOB_RUNNING_OR_FINISH.getMsg());
        }

        checkParam(jobDto);
        //修改作业
        BeanCopierUtils.copy(job, jobOriginal);
        BeanCopierUtils.copy(jobDto, job);
        job.setJobStatus(JobStatusEnum.UN_START.getValue());
        job.setStatus(StatusEnum.VALID.getValue());
        if (job.getRepeatFlag() == JobRepeatEnum.NOREPEAT.getValue()) {
            job.setRepeatInterval(null);
            job.setEndTime(null);
            job.setStatus(StatusEnum.VALID.getValue());
            job.setUpdateTime(new Date());
        }
        if (this.baseMapper.updateJobById(job) == 0) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.JOB_EDIT_ERROR.getMsg());
        }

        //将作业保存至redis待后续处理
        editJobPublish(job);
//        redisUtil.listLeftPush(SysConstant.JOB_EDIT_QUEUE, JSON.toJSONString(job), null);
    }

    @Override
    public List<Workflow> queryRelatedWorkflowName(Long projectId) {
        return workflowService.queryWorkFlowByProjectId(projectId);
    }

    @Override
    public void pause(Long id) {
        Job job = this.getById(id);
        if (job.getJobStatus() != JobStatusEnum.RUNNING.getValue()) {
            log.error("job is not running can not modify by jobId:{}", job.getId());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.JOB_NOT_RUNNING.getMsg());
        }
//        redisUtil.listLeftPush(SysConstant.JOB_PAUSE_QUEUE, JSON.toJSONString(job), null);
        pauseJobPublish(job);
    }

    @Override
    public void reStart(Long id) {
        Job job = this.getById(id);
        if (job.getJobStatus() != JobStatusEnum.STOP.getValue()) {
            log.error("job is not stop can not modify by jobId:{}", job.getId());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.JOB_NOT_STOP.getMsg());
        }
//        redisUtil.listLeftPush(SysConstant.JOB_ADD_QUEUE, JSON.toJSONString(job), null);
        addJobPublish(job);
    }

    @Override
    public List<Job> listRunJobByWorkflowId(Long workflowId) {
        LambdaQueryWrapper<Job> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Job::getWorkflowId, workflowId);
        wrapper.eq(Job::getJobStatus, JobStatusEnum.RUNNING.getValue());
        return this.list(wrapper);
    }

    @Override
    public void deleteBatchJob(List<Long> ids) {
        if (ids.isEmpty()) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.JOB_ID_NOT_EXIST.getMsg());
        }
        List<Job> jobs = this.listByIds(ids);
        if (jobs.isEmpty() || ids.size() != jobs.size()) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.JOB_NOT_EXIST.getMsg());
        }
        //检查作业是否存在运行中
        boolean isExistRunningJob = jobs.stream().anyMatch(job -> job.getJobStatus() == JobStatusEnum.RUNNING.getValue());
        if (isExistRunningJob) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.JOB_NOT_DELETE.getMsg());
        }
        //检查子作业是否存在运行中
        List<SubJob> subJobList = subJobService.queryBatchSubJobListByJobId(ids);
        Object[] subJobArrayIds = subJobList.stream().map(SubJob::getId).toArray();
        boolean isExistRunningSubJob = subJobList.stream().anyMatch(subJob -> subJob.getSubJobStatus() == SubJobStatusEnum.RUNNING.getValue());
        if (isExistRunningSubJob) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.SUB_JOB_NOT_DELETE.getMsg());
        }
        //检查子作业节点是否存在运行中节点
        List<SubJobNode> subJobNodeList = subJobNodeService.queryBatchSubJobListNodeByJobId(subJobArrayIds);
        Object[] subJobNodeArrayIds = subJobNodeList.stream().map(SubJobNode::getId).toArray();
        boolean isExistRunningSubJobNode = subJobNodeList.stream().anyMatch(subJobNode -> subJobNode.getRunStatus() == SubJobNodeStatusEnum.RUNNING.getValue());
        if (isExistRunningSubJobNode) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.SUB_JOB_NODE_NOT_DELETE.getMsg());
        }
        //批量更新作业、子作业、子作业节点有效状态
        this.updateBatchStatus(ids.toArray(), StatusEnum.UN_VALID.getValue());
        subJobService.updateBatchStatus(subJobArrayIds, StatusEnum.UN_VALID.getValue());
        subJobNodeService.updateBatchStatus(subJobNodeArrayIds, StatusEnum.UN_VALID.getValue());
    }

    /**
     * 检查入参合法性
     */
    private void checkParam(JobDto jobDto) {
        //校验开始时间 > 结束时间
        if (!Objects.isNull(jobDto.getBeginTime()) && !Objects.isNull(jobDto.getEndTime()) && jobDto.getBeginTime().after(jobDto.getEndTime())) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.JOB_TIME_ERROR.getMsg());
        }
        //作业执行重复时,结束时间及重复次数合法性
        if (jobDto.getRepeatFlag() == JobRepeatEnum.REPEAT.getValue()) {
            if (Objects.isNull(jobDto.getEndTime()) || Objects.isNull(jobDto.getRepeatInterval())) {
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.JOB_TIME_REPEAT_INTERVAL_ERROR.getMsg());
            }
        } else {
            if (!Objects.isNull(jobDto.getEndTime()) || !Objects.isNull(jobDto.getRepeatInterval())) {
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.JOB_TIME_NO_REPEAT_INTERVAL_ERROR.getMsg());
            }
        }
    }


    @MQPublish(name = SysConstant.JOB_ADD_QUEUE)
    @SuppressWarnings("all")
    public Job addJobPublish(Job job) {
        return job;
    }

    @MQPublish(name = SysConstant.JOB_EDIT_QUEUE)
    @SuppressWarnings("all")
    public Job editJobPublish(Job job) {
        return job;
    }

    @MQPublish(name = SysConstant.JOB_PAUSE_QUEUE)
    @SuppressWarnings("all")
    public Job pauseJobPublish(Job job) {
        return job;
    }
}
