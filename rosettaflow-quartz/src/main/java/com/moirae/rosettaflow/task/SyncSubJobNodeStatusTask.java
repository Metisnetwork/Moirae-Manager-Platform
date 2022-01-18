package com.moirae.rosettaflow.task;

import cn.hutool.core.collection.CollUtil;
import com.moirae.rosettaflow.common.enums.DataSyncTypeEnum;
import com.moirae.rosettaflow.dto.SubJobNodeDto;
import com.moirae.rosettaflow.grpc.service.GrpcTaskService;
import com.moirae.rosettaflow.grpc.task.req.dto.TaskDetailResponseDto;
import com.moirae.rosettaflow.mapper.domain.WorkflowRunTaskStatus;
import com.moirae.rosettaflow.service.IDataSyncService;
import com.moirae.rosettaflow.service.IWorkflowRunStatusService;
import com.zengtengpeng.annotation.Lock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author juzix
 * @description 同步更新子作业节点中运行中的任务
 */
@Slf4j
@Component
public class SyncSubJobNodeStatusTask {

    @Resource
    private GrpcTaskService grpcTaskService;
    @Resource
    private IWorkflowRunStatusService workflowRunStatusService;
    @Resource
    private IDataSyncService dataSyncService;

    @Scheduled(fixedDelay = 30 * 1000, initialDelay = 60 * 1000)
    @Lock(keys = "SyncSubJobNodeStatusTask")
    public void run() {
        List<WorkflowRunTaskStatus> workflowRunTaskStatusList = workflowRunStatusService.queryUnConfirmedWorkflowRunTaskStatus();
        // 执行取消逻辑
        workflowRunTaskStatusList = workflowRunTaskStatusList.stream()
                .filter(item -> !workflowRunStatusService.cancel(item))
                .collect(Collectors.toList());
        //如果没有需要同步的数据则不进行同步
        if (CollUtil.isEmpty(workflowRunTaskStatusList)) {
            return;
        }
        log.info("同步更新子作业节点运行中任务开始>>>>");
        Map<String, Long> workflowRunTaskStatusMap = workflowRunTaskStatusList.stream().collect(Collectors.toMap(WorkflowRunTaskStatus::getTaskId, WorkflowRunTaskStatus::getWorkflowRunId));
        dataSyncService.sync(DataSyncTypeEnum.SUB_JOB_NODE_STATUS.getDataType(),DataSyncTypeEnum.SUB_JOB_NODE_STATUS.getDesc(),//1.根据dataType同步类型获取新的同步时间DataSync
                (latestSynced) -> {//2.根据新的同步时间latestSynced获取分页列表grpcResponseList
                    return grpcTaskService.getTaskDetailList(latestSynced);
                },
                (grpcResponseList) -> {//3.根据分页列表grpcResponseList实现实际业务逻辑
                    for (TaskDetailResponseDto taskDetailResponseDto : grpcResponseList) {
                        String taskId = taskDetailResponseDto.getInformation().getTaskId();
                        int state = taskDetailResponseDto.getInformation().getState();
                        long taskStartAt = taskDetailResponseDto.getInformation().getStartAt();
                        long taskEndAt = taskDetailResponseDto.getInformation().getEndAt();
                        if (workflowRunTaskStatusMap.containsKey(taskId)) {
                            workflowRunStatusService.taskFinish(workflowRunTaskStatusMap.get(taskId), taskId, state, taskStartAt, taskEndAt);
                        }
                    }
                },
                (grpcResponseList) -> {//4.根据分页列表grpcResponseList获取最新的同步时间latestSynced
                    return grpcResponseList
                            .get(grpcResponseList.size() - 1)
                            .getInformation()
                            .getUpdateAt();
                });
        log.info("同步更新子作业节点运行中任务结束>>>>");
    }
}
