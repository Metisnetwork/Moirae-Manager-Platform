package com.moirae.rosettaflow.task;
import com.zengtengpeng.annotation.Lock;
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

    @Scheduled(fixedDelay = 5 * 60 * 1000, initialDelay = 60 * 1000)
    @Lock(keys = "SyncJobStatusTask")
    public void run() {
        jobManager.finishJobBatchWithTask();
    }
}
