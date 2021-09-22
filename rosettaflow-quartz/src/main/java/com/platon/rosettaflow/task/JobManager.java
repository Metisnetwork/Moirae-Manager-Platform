package com.platon.rosettaflow.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.platon.rosettaflow.common.constants.SysConfig;
import com.platon.rosettaflow.common.constants.SysConstant;
import com.platon.rosettaflow.common.enums.*;
import com.platon.rosettaflow.common.exception.BusinessException;
import com.platon.rosettaflow.common.utils.RedisUtil;
import com.platon.rosettaflow.mapper.domain.Job;
import com.platon.rosettaflow.quartz.job.PublishTaskJob;
import com.platon.rosettaflow.service.IJobService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author hudenian
 * @date 2021/8/13
 * @description 作业管理服务
 */
@Slf4j
@Component
public class JobManager {

    static final String GROUP = "MOIRAE";

    @Resource
    private Scheduler scheduler;

    @Resource
    private SysConfig sysConfig;

    @Resource
    private IJobService jobService;

    @Resource
    private RedisUtil redisUtil;

    /**
     * 服务启动加载所有的job
     */
    @PostConstruct
    public void init() {
        if (sysConfig.isMasterNode()) {
            //服务启动，清除缓存作业消息队列
            boolean isDelete = redisUtil.deleteBatch(Arrays.asList(SysConstant.JOB_ADD_QUEUE, SysConstant.JOB_EDIT_QUEUE));
            if(!isDelete){
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.JOB_RUNNING_CACHE_CLEAR_ERROR.getMsg());
            }
            //服务启动，启动所有未完成作业
            List<Job> jobList = jobService.getAllUnfinishedJob();
            for (Job job : jobList) {
                startJob(job);
            }
        }
    }

    /**
     * 创建一个job信息
     *
     * @param job job信息
     */
    public void startJob(Job job) {
        stopJob(job);
        Long workflowId = job.getWorkflowId();

        JobDetail jobDetail = JobBuilder.newJob(PublishTaskJob.class)
                .usingJobData("workflowId", workflowId)
                .usingJobData("jobId", job.getId())
                .withIdentity(job.getId().toString(), GROUP)
                .build();

        SimpleScheduleBuilder simpleScheduleBuilder;
        if (JobRepeatEnum.NOREPEAT.getValue() == job.getRepeatFlag()) {
            simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                    .withRepeatCount(1);
        } else {
            simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                    .withIntervalInMinutes(job.getRepeatInterval())
                    .repeatForever();
        }

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(job.getId().toString(),GROUP)
                .startAt(job.getBeginTime())
                .endAt(job.getEndTime())
                .withSchedule(simpleScheduleBuilder)
                .build();
        try {
            scheduler.scheduleJob(jobDetail, trigger);
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
            log.info("作业id:{}启动成功>>>>>>", job.getId());
        } catch (SchedulerException e) {
            log.error("作业id:{}启动失败>>>>>>", job.getId(), e);
        }
    }

    /**
     * 停止作业
     *
     * @param job 作业信息
     */
    private void stopJob(Job job) {
        try {
            Set<JobKey> jobKeySet = scheduler.getJobKeys(GroupMatcher.groupEquals(GROUP));
            if (jobKeySet.contains(JobKey.jobKey(job.getId().toString(), GROUP))) {
                try {
                    //暂停触发器
                    scheduler.pauseTrigger(TriggerKey.triggerKey(job.getId().toString(), GROUP));
                    //移除触发器
                    scheduler.unscheduleJob(TriggerKey.triggerKey(job.getId().toString(), GROUP));
                    //删除Job
                    scheduler.deleteJob(JobKey.jobKey(job.getId().toString(), GROUP));
                } catch (SchedulerException e) {
                    log.error("stop job error,error msg is:{}", e.getMessage(), e);
                }
            }
        } catch (SchedulerException e) {
            log.error("获取正在执行的job失败:失败原因>>>>>>", e);
        }
    }

    /**
     * 暂停job
     *
     * @param job job信息
     */
    public void pauseJob(Job job) {
        stopJob(job);
        job.setJobStatus(JobStatusEnum.STOP.getValue());
        jobService.updateById(job);

    }

    /**
     * 完成job
     * @param job job信息
     */
    public void finishJob(Job job){
        job.setJobStatus(JobStatusEnum.FINISH.getValue());
        jobService.updateById(job);
    }

    /**
     * 批量完成job
     * @param jobList job集合信息
     */
    public void finishJobBatch(List<Job> jobList){
        jobList.forEach(job ->
            job.setJobStatus(JobStatusEnum.FINISH.getValue())
        );
        jobService.updateBatchById(jobList);
    }




    /**
     * 批量结束job(定时任务也使用)
     */
    public void finishJobBatchWithTask(){
        try{
            //1、获取调度任务所有作业
            List<Long> schedulerJobIdList = new ArrayList<>();
            Set<JobKey> jobKeySet = scheduler.getJobKeys(GroupMatcher.groupEquals(JobManager.GROUP));
            jobKeySet.forEach(jobKey ->
                    schedulerJobIdList.add(Long.valueOf(jobKey.getName()))
            );
            //2、获取所有状态为运行中作业
            LambdaQueryWrapper<Job> jobQueryWrapper = Wrappers.lambdaQuery();
            jobQueryWrapper.eq(Job::getJobStatus, JobStatusEnum.RUNNING.getValue());
            jobQueryWrapper.eq(Job::getStatus, StatusEnum.VALID.getValue());
            List<Job> jobList = jobService.list(jobQueryWrapper);
            //3、筛选出状态为结束作业
            List<Job> jobFinishList = jobList.stream().filter(job ->
                    !schedulerJobIdList.contains(job.getId())
            ).collect(Collectors.toList());
            //4、批量更新作业状态
            if(!jobFinishList.isEmpty()){
                finishJobBatch(jobFinishList);
            }

        }catch (Exception e){
            log.error("SyncJobStatusTask error {}", e.getMessage(),e);
        }
    }
}
