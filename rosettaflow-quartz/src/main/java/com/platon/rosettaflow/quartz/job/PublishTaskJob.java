package com.platon.rosettaflow.quartz.job;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.platon.rosettaflow.common.enums.JobStatusEnum;
import com.platon.rosettaflow.common.enums.StatusEnum;
import com.platon.rosettaflow.common.enums.SubJobStatusEnum;
import com.platon.rosettaflow.common.utils.BeanCopierUtils;
import com.platon.rosettaflow.dto.WorkflowDto;
import com.platon.rosettaflow.mapper.domain.SubJob;
import com.platon.rosettaflow.mapper.domain.Workflow;
import com.platon.rosettaflow.service.IJobService;
import com.platon.rosettaflow.service.ISubJobService;
import com.platon.rosettaflow.service.IWorkflowService;
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
        log.info("开始处理作业,作业id:{},工作流id:{}", jobId, workflowId);

        Workflow workFlow = workflowService.getById(workflowId);
        if (null == workFlow) {
            log.error("workFlow can not find by workflow id:{}", workflowId);
            return;
        }

        //记录作业
        LambdaUpdateWrapper<com.platon.rosettaflow.mapper.domain.Job> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(com.platon.rosettaflow.mapper.domain.Job::getId, jobId);
        updateWrapper.set(com.platon.rosettaflow.mapper.domain.Job::getJobStatus, JobStatusEnum.RUNNING.getValue());
        jobService.update(updateWrapper);

        //记录子作业
        SubJob subJob = new SubJob();
        subJob.setJobId(jobId);
        subJob.setWorkflowId(workflowId);
        subJob.setBeginTime(now());
        subJob.setSubJobStatus(SubJobStatusEnum.RUNNING.getValue());
        subJob.setStatus(StatusEnum.VALID.getValue());
        subJobService.save(subJob);
        log.info("处理作业,作业id:{},工作流id:{},记录子作业表id:{}", jobId, workflowId, subJob.getId());

        //启动工作流
        WorkflowDto workflowDto = new WorkflowDto();
        BeanCopierUtils.copy(workFlow, workflowDto);
        workflowDto.setStartNode(1);
        workflowDto.setEndNode(workFlow.getNodeNumber());
        workflowDto.setJobFlg(true);
        workflowService.start(workflowDto);
        log.info("处理作业,作业id:{},工作流id:{},记录子作业表id:{},此工作流一共有{}个节点,当前运行第{}个节点", jobId, workflowId, subJob.getId(), workflowDto.getEndNode(), workflowDto.getStartNode());
    }
}
