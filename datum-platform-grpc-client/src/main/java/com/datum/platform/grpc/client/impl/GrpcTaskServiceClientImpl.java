package com.datum.platform.grpc.client.impl;

import com.datum.platform.common.exception.BusinessException;
import com.datum.platform.grpc.client.GrpcTaskServiceClient;
import com.datum.platform.grpc.constant.GrpcConstant;
import com.datum.platform.grpc.service.*;
import com.datum.platform.grpc.service.types.SimpleResponse;
import com.datum.platform.grpc.service.types.TaskDetail;
import com.datum.platform.grpc.service.types.TaskEvent;
import io.grpc.Channel;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author admin
 * @date 2021/9/1
 * @description 任务处理相关接口
 */
@Slf4j
@Component
public class GrpcTaskServiceClientImpl implements GrpcTaskServiceClient {

    @GrpcClient("carrier-grpc-server")
    TaskServiceGrpc.TaskServiceBlockingStub taskServiceBlockingStub;

    @GrpcClient("carrier-grpc-server")
    TaskServiceGrpc.TaskServiceStub taskServiceStub;

    @Override
    public List<TaskDetail> getGlobalTaskDetailList(Long latestSynced) {
        GetTaskDetailListRequest request = GetTaskDetailListRequest.newBuilder()
                .setLastUpdated(latestSynced)
                .setPageSize(GrpcConstant.PAGE_SIZE)
                .build();
        GetTaskDetailListResponse response = taskServiceBlockingStub.getGlobalTaskDetailList(request);

        if (response.getStatus() != GrpcConstant.GRPC_SUCCESS_CODE) {
            log.error("GrpcTaskServiceClientImpl->getTaskDetailList() fail reason:{}", response.getMsg());
            throw new BusinessException(response.getStatus(), response.getMsg());
        }
        return response.getTasksList();
    }

    @Override
    public List<TaskEvent> getTaskEventList(String taskId) {
        GetTaskEventListRequest getTaskEventListRequest = GetTaskEventListRequest.newBuilder().setTaskId(taskId).build();
        GetTaskEventListResponse response = taskServiceBlockingStub.getTaskEventList(getTaskEventListRequest);

        if (response.getStatus() != GrpcConstant.GRPC_SUCCESS_CODE) {
            log.error("GrpcTaskServiceClientImpl->getTaskDetailList() fail reason:{}", response.getMsg());
            throw new BusinessException(response.getStatus(), response.getMsg());
        }
        return response.getTaskEventsList();
    }

    @Override
    public PublishTaskDeclareResponse publishTaskDeclare(Channel channel, PublishTaskDeclareRequest request) {
        log.info("publishTaskDeclare req = {}", request);
        PublishTaskDeclareResponse response = TaskServiceGrpc.newBlockingStub(channel).publishTaskDeclare(request);
        log.info("publishTaskDeclare resp = {}", response);
        return response;
    }

    @Override
    public SimpleResponse terminateTask(Channel channel, TerminateTaskRequest request) {
        return TaskServiceGrpc.newBlockingStub(channel).terminateTask(request);
    }

    @Override
    public EstimateTaskGasResponse estimateTaskGas(Channel channel, EstimateTaskGasRequest request) {
        EstimateTaskGasResponse response = TaskServiceGrpc.newBlockingStub(channel).estimateTaskGas(request);
        if (response.getStatus() != GrpcConstant.GRPC_SUCCESS_CODE) {
            log.error("GrpcTaskServiceClientImpl->estimateTaskGas() fail reason:{}", response.getMsg());
            throw new BusinessException(response.getStatus(), response.getMsg());
        }
        return response;
    }
}
