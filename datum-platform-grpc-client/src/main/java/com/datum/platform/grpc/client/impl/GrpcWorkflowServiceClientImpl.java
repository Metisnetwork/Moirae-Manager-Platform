package com.datum.platform.grpc.client.impl;

import carrier.api.WorkFlowServiceGrpc;
import carrier.api.WorkflowRpcApi;
import com.datum.platform.grpc.client.GrpcWorkflowServiceClient;
import common.constant.CarrierEnum;
import io.grpc.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liushuyu
 * @date 2022/9/29 17:49
 * @desc
 */

@Slf4j
@Component
public class GrpcWorkflowServiceClientImpl implements GrpcWorkflowServiceClient {

    @Override
    public WorkflowRpcApi.PublishWorkFlowDeclareResponse publishWorkFlowDeclare(Channel channel, WorkflowRpcApi.PublishWorkFlowDeclareRequest request) {
        log.info("publishWorkFlowDeclare req = {}", request);
        WorkflowRpcApi.PublishWorkFlowDeclareResponse response = WorkFlowServiceGrpc.newBlockingStub(channel).publishWorkFlowDeclare(request);
        log.info("publishWorkFlowDeclare resp = {}", response);
        return response;
    }

    @Override
    public WorkflowRpcApi.QueryWorkStatusResponse queryWorkFlowStatus(Channel channel, WorkflowRpcApi.QueryWorkStatusRequest request) {
        log.info("queryWorkFlowStatus req = {}", request);
        WorkflowRpcApi.QueryWorkStatusResponse response = WorkFlowServiceGrpc.newBlockingStub(channel).queryWorkFlowStatus(request);
        log.info("queryWorkFlowStatus resp = {}", response);
        return response;
    }

    public static void main(String[] args) {
        //工作流任务List
        List<WorkflowRpcApi.WorkFlowTaskStatus> taskList = new ArrayList<>();
        WorkflowRpcApi.WorkFlowTaskStatus task1 = WorkflowRpcApi.WorkFlowTaskStatus.newBuilder()
                .setTaskId("taskId1")
                .setStatus(CarrierEnum.TaskState.TaskState_Failed)
                .setStartAt(1000)
                .setEndAt(2000)
                .build();
        WorkflowRpcApi.WorkFlowTaskStatus task2 = WorkflowRpcApi.WorkFlowTaskStatus.newBuilder()
                .setTaskId("taskId2")
                .setStatus(CarrierEnum.TaskState.TaskState_Failed)
                .setStartAt(1000)
                .setEndAt(2000)
                .build();
        taskList.add(task1);
        taskList.add(task2);

        //工作流List
        List<WorkflowRpcApi.WorkFlowStatus> workflowList = new ArrayList<>();
        WorkflowRpcApi.WorkFlowStatus workFlowStatus = WorkflowRpcApi.WorkFlowStatus.newBuilder()
                .setStatus(CarrierEnum.WorkFlowState.WorkFlowState_Succeed)
                .addAllTaskList(taskList)
                .build();
        workflowList.add(workFlowStatus);

        WorkflowRpcApi.QueryWorkStatusResponse response = WorkflowRpcApi.QueryWorkStatusResponse.newBuilder()
                .setMsg("1")
                .setStatus(0)
                .addAllWorkflowStatusList(workflowList)
                .build();
        System.out.println(response);
    }
}
