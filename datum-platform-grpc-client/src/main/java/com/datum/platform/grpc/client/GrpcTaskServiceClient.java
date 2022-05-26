package com.datum.platform.grpc.client;

import com.datum.platform.grpc.service.*;
import com.datum.platform.grpc.service.types.SimpleResponse;
import com.datum.platform.grpc.service.types.TaskDetail;
import com.datum.platform.grpc.service.types.TaskEvent;
import io.grpc.Channel;

import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/24
 * @description grpc任务处理服务
 */
public interface GrpcTaskServiceClient {

    List<TaskDetail> getGlobalTaskDetailList(Long latestSynced);

    List<TaskEvent> getTaskEventList(String taskId);

    PublishTaskDeclareResponse publishTaskDeclare(Channel channel, PublishTaskDeclareRequest request);

    SimpleResponse terminateTask(Channel channel, TerminateTaskRequest request);

    EstimateTaskGasResponse estimateTaskGas(Channel channel, EstimateTaskGasRequest request);
}
