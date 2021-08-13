package com.platon.rosettaflow.quartz.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.util.Date;

/**
 * @author hudenian
 * @date 2021/8/13
 * @description 功能描述
 */
@DisallowConcurrentExecution
public class PublishTaskJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        System.err.println(jobExecutionContext.getJobDetail().getJobDataMap().get("workflowId"));
        System.err.println(jobExecutionContext.getJobDetail().getJobDataMap().get("jobId"));
        System.err.println(jobExecutionContext.getJobDetail().getJobDataMap().get("repeatFlg"));

        //TODO 拼装发布任务数据，执行成功更新任务表及对应的工作流表信息
        System.err.println("当前线程：" + Thread.currentThread().getName() + "定时任务执行，当前时间：" + new Date());
    }
}
