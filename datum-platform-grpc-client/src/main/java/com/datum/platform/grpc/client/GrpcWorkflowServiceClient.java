package com.datum.platform.grpc.client;

import carrier.api.WorkflowRpcApi;
import io.grpc.Channel;

/**
 * grpc工作流处理服务
 */
public interface GrpcWorkflowServiceClient {

    WorkflowRpcApi.PublishWorkFlowDeclareResponse publishWorkFlowDeclare(Channel channel, WorkflowRpcApi.PublishWorkFlowDeclareRequest request);

    WorkflowRpcApi.QueryWorkStatusResponse queryWorkFlowStatus(Channel channel, WorkflowRpcApi.QueryWorkStatusRequest request);
}
