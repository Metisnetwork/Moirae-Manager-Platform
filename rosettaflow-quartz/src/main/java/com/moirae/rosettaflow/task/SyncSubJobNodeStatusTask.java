package com.moirae.rosettaflow.task;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.moirae.rosettaflow.grpc.service.GrpcTaskService;
import com.moirae.rosettaflow.grpc.task.req.dto.TaskDetailResponseDto;
import com.moirae.rosettaflow.mapper.domain.WorkflowRunTaskStatus;
import com.moirae.rosettaflow.service.IDataSyncService;
import com.moirae.rosettaflow.service.IWorkflowRunStatusService;
import com.moirae.rosettaflow.service.NetManager;
import com.zengtengpeng.annotation.Lock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
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
    @Resource
    private NetManager netManager;

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
        // 通道 -> List<任务>
        Map<String, List<WorkflowRunTaskStatus>> channelTaskSetMap = workflowRunTaskStatusList.stream().collect(Collectors.groupingBy(WorkflowRunTaskStatus::getSenderIdentityId));
        //获取所有任务详情
        for (String identityId: channelTaskSetMap.keySet()) {
            // 任务id -> workflowRunId
            Map<String, Long> workflowRunTaskStatusMap = channelTaskSetMap.get(identityId).stream().collect(Collectors.toMap(WorkflowRunTaskStatus::getTaskId, WorkflowRunTaskStatus::getWorkflowRunId));
//            List<TaskDetailResponseDto> taskDetailResponseDtoList = grpcTaskService.getTaskDetailList(netManager.getChannel(identityId));
            List<TaskDetailResponseDto> taskDetailResponseDtoList = new ArrayList<>();
            for (TaskDetailResponseDto taskDetailResponseDto : taskDetailResponseDtoList) {
                String taskId = taskDetailResponseDto.getInformation().getTaskId();
                int state = taskDetailResponseDto.getInformation().getState();
                long taskStartAt = taskDetailResponseDto.getInformation().getStartAt();
                long taskEndAt = taskDetailResponseDto.getInformation().getEndAt();
                if (workflowRunTaskStatusMap.containsKey(taskId)) {
                    try{
                        log.info("同步更新子作业节点运行中任务开始 taskId = {}, result = {}", taskId, JSONObject.toJSON(taskDetailResponseDto));
                        workflowRunStatusService.taskFinish(workflowRunTaskStatusMap.get(taskId), taskId, state, taskStartAt, taskEndAt);
                    }catch (Exception e){
                        log.error("同步更新子作业节点运行中任务异常 taskId = " + taskId, e);
                    }
                }
            }
        }
        log.info("同步更新子作业节点运行中任务结束>>>>");
    }
}
