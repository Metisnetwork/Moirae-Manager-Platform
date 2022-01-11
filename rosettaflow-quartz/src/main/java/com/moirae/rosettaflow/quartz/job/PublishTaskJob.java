package com.moirae.rosettaflow.quartz.job;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.moirae.rosettaflow.common.enums.JobStatusEnum;
import com.moirae.rosettaflow.common.enums.SubJobStatusEnum;
import com.moirae.rosettaflow.common.utils.BeanCopierUtils;
import com.moirae.rosettaflow.dto.WorkflowDto;
import com.moirae.rosettaflow.mapper.domain.SubJob;
import com.moirae.rosettaflow.mapper.domain.Workflow;
import com.moirae.rosettaflow.service.IJobService;
import com.moirae.rosettaflow.service.ISubJobService;
import com.moirae.rosettaflow.service.IWorkflowService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import javax.annotation.Resource;

import static cn.hutool.core.date.DateTime.now;

/**
 * @author hudenian
 * @date 2021/8/13
 * @description 发布任务Job
 */
@Slf4j
@DisallowConcurrentExecution
public class PublishTaskJob implements Job {

    @Resource
    private IWorkflowService workflowService;

    @Resource
    private ISubJobService subJobService;

    @Resource
    private IJobService jobService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        Long workflowId = (Long) jobExecutionContext.getJobDetail().getJobDataMap().get("workflowId");
        Long jobId = (Long) jobExecutionContext.getJobDetail().getJobDataMap().get("jobId");
        log.info("PublishTaskJob->execute:开始处理作业,作业id:{},工作流id:{}", jobId, workflowId);

        Workflow workFlow = workflowService.getById(workflowId);
        if (null == workFlow) {
            log.error("workFlow can not find by workflow id:{}", workflowId);
            return;
        }

        //记录作业
        LambdaUpdateWrapper<com.moirae.rosettaflow.mapper.domain.Job> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(com.moirae.rosettaflow.mapper.domain.Job::getId, jobId);
        updateWrapper.set(com.moirae.rosettaflow.mapper.domain.Job::getJobStatus, JobStatusEnum.RUNNING.getValue());
        jobService.update(updateWrapper);

        //记录子作业
        SubJob subJob = new SubJob();
        subJob.setJobId(jobId);
        subJob.setWorkflowId(workflowId);
        subJob.setBeginTime(now());
        subJob.setSubJobStatus(SubJobStatusEnum.RUNNING.getValue());
        subJobService.save(subJob);
        log.info("PublishTaskJob->execute:处理作业,作业id:{},工作流id:{},记录子作业表id:{}", jobId, workflowId, subJob.getId());

        //启动工作流
        WorkflowDto workflowDto = new WorkflowDto();
        BeanCopierUtils.copy(workFlow, workflowDto);
        workflowDto.setStartNode(1);
        //todo
//        workflowDto.setEndNode(workFlow.getNodeNumber());
        workflowDto.setJobFlg(true);
        workflowDto.setSubJobId(subJob.getId());
        workflowService.start(workflowDto);
        log.info("PublishTaskJob->execute:处理作业,作业id:{},工作流id:{},记录子作业表id:{},此工作流一共有{}个节点,当前运行第{}个节点", jobId, workflowId, subJob.getId(), workflowDto.getEndNode(), workflowDto.getStartNode());
    }
}
