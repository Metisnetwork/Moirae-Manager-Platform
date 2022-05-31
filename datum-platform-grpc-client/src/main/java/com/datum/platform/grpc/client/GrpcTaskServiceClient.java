package com.datum.platform.grpc.client;

import carrier.api.TaskRpcApi;
import carrier.types.Common;
import carrier.types.Taskdata;
import io.grpc.Channel;

import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/24
 * @description grpc任务处理服务
 */
public interface GrpcTaskServiceClient {

    List<Taskdata.TaskDetail> getGlobalTaskDetailList(Long latestSynced);

    List<Taskdata.TaskEvent> getTaskEventList(String taskId);

    TaskRpcApi.PublishTaskDeclareResponse publishTaskDeclare(Channel channel, TaskRpcApi.PublishTaskDeclareRequest request);

    Common.SimpleResponse terminateTask(Channel channel, TaskRpcApi.TerminateTaskRequest request);

    TaskRpcApi.EstimateTaskGasResponse estimateTaskGas(Channel channel, TaskRpcApi.EstimateTaskGasRequest request);
}
