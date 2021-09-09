package com.platon.rosettaflow.service.Impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.common.constants.SysConstant;
import com.platon.rosettaflow.common.enums.*;
import com.platon.rosettaflow.common.exception.BusinessException;
import com.platon.rosettaflow.common.utils.BeanCopierUtils;
import com.platon.rosettaflow.common.utils.RedisUtil;
import com.platon.rosettaflow.dto.JobDto;
import com.platon.rosettaflow.mapper.JobMapper;
import com.platon.rosettaflow.mapper.domain.Job;
import com.platon.rosettaflow.mapper.domain.Workflow;
import com.platon.rosettaflow.service.IJobService;
import com.platon.rosettaflow.service.IWorkflowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author hudenian
 * @date 2021/8/13
 * @description 作业处理类
 */
@Slf4j
@Service
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements IJobService {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private IWorkflowService workflowService;

    @Override
    public List<Job> getAllUnfinishedJob() {
        LambdaQueryWrapper<Job> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Job::getJobStatus, JobStatusEnum.UN_START.getValue());
        wrapper.le(Job::getBeginTime, new Date(System.currentTimeMillis()));
        wrapper.ge(Job::getEndTime, new Date(System.currentTimeMillis()));
        wrapper.eq(Job::getStatus, StatusEnum.VALID.getValue());
        wrapper.orderByAsc(Job::getId);
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public IPage<JobDto> list(Long current, Long size, String jobName) {
        Page<JobDto> jobPage = new Page<>(current, size);
        return this.baseMapper.queryJobList(jobName, jobPage);
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
        redisUtil.listLeftPush(SysConstant.JOB_ADD_QUEUE, JSON.toJSONString(job), null);
    }

    @Override
    public void edit(JobDto jobDto) {
        Job job = this.getById(jobDto.getId());
        if (null == job) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.JOB_NOT_EXIST.getMsg());
        }
        checkParam(jobDto);
        //修改作业
        BeanCopierUtils.copy(jobDto, job);
        job.setJobStatus(JobStatusEnum.UN_START.getValue());
        if (!this.updateById(job)) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.JOB_EDIT_ERROR.getMsg());
        }

        //将作业保存至redis待后续处理
        redisUtil.listLeftPush(SysConstant.JOB_EDIT_QUEUE, JSON.toJSONString(job), null);
    }

    @Override
    public List<Workflow> queryRelatedWorkflowName(Long projectId) {
        return workflowService.queryWorkFlowByProjectId(projectId);
    }

    @Override
    public void pause(Long id) {
        Job job = this.getById(id);
        if (job.getJobStatus() == JobStatusEnum.RUNNING.getValue()) {
            redisUtil.listLeftPush(SysConstant.JOB_PAUSE_QUEUE, JSON.toJSONString(job), null);
        }
    }

    @Override
    public void reStart(Long id) {
        Job job = this.getById(id);
        if (job.getJobStatus() == JobStatusEnum.STOP.getValue()) {
            redisUtil.listLeftPush(SysConstant.JOB_ADD_QUEUE, JSON.toJSONString(job), null);
        }
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
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.JOB_TIME_REPEATINTERVAL_ERROR.getMsg());
            }
        }
    }

}
