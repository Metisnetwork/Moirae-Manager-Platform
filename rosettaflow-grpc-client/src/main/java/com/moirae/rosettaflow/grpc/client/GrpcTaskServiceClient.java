package com.moirae.rosettaflow.grpc.client;

import com.moirae.rosettaflow.common.exception.BusinessException;
import com.moirae.rosettaflow.grpc.constant.GrpcConstant;
import com.moirae.rosettaflow.grpc.service.*;
import com.moirae.rosettaflow.grpc.service.types.SimpleResponse;
import com.moirae.rosettaflow.grpc.task.req.dto.*;
import com.moirae.rosettaflow.grpc.task.resp.dto.PublishTaskDeclareResponseDto;
import io.grpc.Channel;
import io.grpc.ManagedChannel;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author hudenian
 * @date 2021/8/24
 * @description grpc任务处理服务
 */
public interface GrpcTaskServiceClient {

    List<GetTaskDetail> getGlobalTaskDetailList(Long latestSynced);

    List<TaskEventShow> getTaskEventList(String taskId);

    PublishTaskDeclareResponse publishTaskDeclare(Channel channel, PublishTaskDeclareRequest request);

    SimpleResponse terminateTask(Channel channel, TerminateTaskRequest request);

    EstimateTaskGasResponse estimateTaskGas(Channel channel, EstimateTaskGasRequest request);
}
