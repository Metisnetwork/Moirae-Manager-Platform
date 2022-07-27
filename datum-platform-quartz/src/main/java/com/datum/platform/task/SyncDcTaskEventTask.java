package com.datum.platform.task;

import cn.hutool.core.date.DateUtil;
import com.datum.platform.mapper.domain.Task;
import com.datum.platform.mapper.domain.TaskEvent;
import com.datum.platform.service.TaskService;
import com.zengtengpeng.annotation.Lock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@ConditionalOnProperty(name="dev.quartz", havingValue="true")
@Slf4j
@Component
public class SyncDcTaskEventTask {

    @Resource
    private TaskService taskService;

//    @Scheduled(fixedDelay = 5 * 1000)
    @Lock(keys = "SyncDcTaskEventTask")
    public void run() {
        long begin = DateUtil.current();
        try {
            List<Task> taskList = taskService.getTaskListOfEventNotSynced();
            for (Task task : taskList) {
                List<TaskEvent> taskEventList = taskService.getTaskEventListFromRemote(task.getId(), task.getOwnerIdentityId());
                batchUpdateTask(taskEventList, task.getId());
            }
        } catch (Exception e) {
            log.error("SyncDcTaskEventTask 任务失败,失败原因：{}", e.getMessage(), e);
        }
        log.info("SyncDcTaskEventTask 任务结束，总耗时:{}ms", DateUtil.current() - begin);
    }

    private void batchUpdateTask(List<TaskEvent> taskEventList, String taskId) {
        taskService.syncedEvent(taskId, taskEventList);
    }
}
