package com.platon.rosettaflow.task;

import com.platon.rosettaflow.common.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * @author juzix
 * @description 同步更新t_job表作业状态【定时检查调度任务scheduler中作业是否执行完毕，并同步更新job_status】
 */
@Slf4j
@Component
public class SyncJobStatusTask {

    @Resource
    private JobManager jobManager;

    @Resource
    private RedisUtil redisUtil;

    @Scheduled(fixedDelay = 5 * 60 * 1000, initialDelay = 60 * 1000)
    public void run() {
        try {
            if (redisUtil.lock(this.getClass().getSimpleName(), this.getClass().getSimpleName())) {
                jobManager.finishJobBatchWithTask();
            }
        } catch (Exception e) {
            log.error("同步更新t_job表作业状态失败，失败原因：{}", e.getMessage(), e);
        } finally {
            redisUtil.unLock(this.getClass().getSimpleName(), this.getClass().getSimpleName());
        }

    }


}
