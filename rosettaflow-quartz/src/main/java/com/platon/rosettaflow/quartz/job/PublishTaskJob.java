package com.platon.rosettaflow.quartz.job;

import com.platon.rosettaflow.service.IWorkflowService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import javax.annotation.Resource;
import java.util.Date;

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

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        Long workflowId = (Long) jobExecutionContext.getJobDetail().getJobDataMap().get("workflowId");
        Long jobId = (Long) jobExecutionContext.getJobDetail().getJobDataMap().get("jobId");
        boolean repeatFlg = (boolean) jobExecutionContext.getJobDetail().getJobDataMap().get("repeatFlg");
        log.info("开始处理作业,作业id:{},工作流id:{},此作业是否只执行一次：{}", jobId, workflowId, repeatFlg ? "是" : "否");


        //TODO 拼装发布任务数据，执行成功更新任务表及对应的工作流表信息
        System.err.println("当前线程：" + Thread.currentThread().getName() + "定时任务执行，当前时间：" + new Date());
    }
}
