package com.platon.rosettaflow.task;

import com.platon.rosettaflow.common.constants.SysConstant;
import com.platon.rosettaflow.mapper.domain.Job;
import com.zengtengpeng.annotation.Lock;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author hudenian
 * @date 2021/8/27
 * @description 监听redis中的新增及修改job任务，并根据任务创建quartz来管理作业
 */
@Slf4j
@Component
public class ListenJobTask {

    @Resource
    private JobManager jobManager;

    @Resource
    private RedissonClient redissonClient;


    /**
     * 注意：测试时候当注释定时任务时，同步注释掉SyncJobStatusTask任务，否则可能出现数据状态不一致
     */
    @Scheduled(fixedDelay = 60 * 60 * 1000, initialDelay = 11 * 1000)
    @Lock(keys = "ListenJobTask-add-queue")
    @SuppressWarnings("InfiniteLoopStatement")
    public void listenAddJob() {
        while (true) {
            try {
                Job job = (Job) redissonClient.getBlockingQueue(SysConstant.JOB_ADD_QUEUE).take();
                log.info("------redis message add queue jobId:" + job.getId());
                jobManager.startJob(job);
            } catch (InterruptedException e) {
                log.error("从redis JOB_ADD_QUEUE队列中获取添加作业失败，失败原因：{}", e.getMessage(), e);
            }
        }
    }

    @Scheduled(fixedDelay = 60 * 70 * 1000, initialDelay = 11 * 1000)
    @Lock(keys = "ListenJobTask-edit-queue")
    @SuppressWarnings("InfiniteLoopStatement")
    public void listenEditJob() {
        while (true) {
            try {
                Job job = (Job) redissonClient.getBlockingQueue(SysConstant.JOB_EDIT_QUEUE).take();
                log.info("------redis message edit queue jobId:" + job.getId());
                jobManager.startJob(job);
            } catch (InterruptedException e) {
                log.error("从redis JOB_EDIT_QUEUE队列中获取添加作业失败，失败原因：{}", e.getMessage(), e);
            }
        }
    }

    @Scheduled(fixedDelay = 60 * 80 * 1000, initialDelay = 11 * 1000)
    @Lock(keys = "ListenJobTask-pause-queue")
    @SuppressWarnings("InfiniteLoopStatement")
    public void listenPauseJob() {
        while (true) {
            try {
                Job job = (Job) redissonClient.getBlockingQueue(SysConstant.JOB_PAUSE_QUEUE).take();
                log.info("------redis message add queue jobId:" + job.getId());
                jobManager.pauseJob(job);
            } catch (InterruptedException e) {
                log.error("从redis JOB_PAUSE_QUEUE队列中获取添加作业失败，失败原因：{}", e.getMessage(), e);
            }
        }
    }
}
