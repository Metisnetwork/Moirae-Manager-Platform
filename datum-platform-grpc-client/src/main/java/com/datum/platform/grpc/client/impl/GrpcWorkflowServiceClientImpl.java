package com.datum.platform.grpc.client.impl;

import carrier.api.WorkFlowServiceGrpc;
import carrier.api.WorkflowRpcApi;
import com.datum.platform.grpc.client.GrpcWorkflowServiceClient;
import io.grpc.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
        log.debug("publishWorkFlowDeclare req = {}", request);
        WorkflowRpcApi.PublishWorkFlowDeclareResponse response = WorkFlowServiceGrpc.newBlockingStub(channel).publishWorkFlowDeclare(request);
        log.debug("publishWorkFlowDeclare resp = {}", response);
        return response;
    }

    @Override
    public WorkflowRpcApi.QueryWorkStatusResponse queryWorkFlowStatus(Channel channel, WorkflowRpcApi.QueryWorkStatusRequest request) {
        log.debug("queryWorkFlowStatus req = {}", request);
        WorkflowRpcApi.QueryWorkStatusResponse response = WorkFlowServiceGrpc.newBlockingStub(channel).queryWorkFlowStatus(request);
        log.debug("queryWorkFlowStatus resp = {}", response);
        return response;
    }

}
