package com.datum.platform.task;

import cn.hutool.core.collection.CollUtil;
import com.datum.platform.mapper.domain.Task;
import com.datum.platform.mapper.domain.WorkflowRunTaskStatus;
import com.datum.platform.mapper.enums.TaskStatusEnum;
import com.datum.platform.service.TaskService;
import com.datum.platform.service.WorkflowService;
import com.zengtengpeng.annotation.Lock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author juzix
 * @description 同步更新子作业节点中运行中的任务
 */
@Slf4j
@Component
@ConditionalOnProperty(name="dev.quartz", havingValue="true")
public class ProcessWorkflowRunStatusTask {

    @Resource
    private WorkflowService workflowService;
    @Resource
    private TaskService taskService;

    @Scheduled(fixedDelay = 5 * 1000)
    @Lock(keys = "ProcessWorkflowRunStatusTask")
    public void run() {
        List<WorkflowRunTaskStatus> workflowRunTaskStatusList =  workflowService.listWorkflowRunTaskStatusOfUnConfirmed();
        // 执行取消逻辑
        workflowRunTaskStatusList = workflowRunTaskStatusList.stream()
                .filter(item -> !workflowService.cancelWorkflowRunTaskStatus(item))
                .collect(Collectors.toList());
        //如果没有需要同步的数据则不进行同步
        if (CollUtil.isEmpty(workflowRunTaskStatusList)) {
            return;
        }

        log.info("同步更新子作业节点运行中任务开始>>>>");

        workflowRunTaskStatusList.forEach(item->{
            try {
                // 查询任务状态
                Task task = taskService.getTaskById(item.getTaskId());
                if(task != null && ( task.getStatus() == TaskStatusEnum.FAILED || task.getStatus() == TaskStatusEnum.SUCCEED)){
                    workflowService.taskFinish(item, task);
                }
            } catch (Exception e){
                log.error("同步更新子作业节点运行中任务异常 taskId = " + item.getTaskId(), e);
            }
        });
        log.info("同步更新子作业节点运行中任务结束>>>>");
    }
}
