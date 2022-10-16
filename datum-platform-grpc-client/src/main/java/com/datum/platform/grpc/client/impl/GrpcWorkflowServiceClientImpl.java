package com.datum.platform.grpc.client.impl;

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
        log.info("publishWorkFlowDeclare req = {}", request);
        WorkflowRpcApi.PublishWorkFlowDeclareResponse response = null;
        log.info("publishWorkFlowDeclare resp = {}", response);
        return null;
    }

    @Override
    public WorkflowRpcApi.QueryWorkStatusResponse queryWorkFlowStatus(Channel channel, WorkflowRpcApi.QueryWorkStatusRequest request) {
        log.info("queryWorkFlowStatus req = {}", request);
        WorkflowRpcApi.QueryWorkStatusResponse response = null;
        log.info("queryWorkFlowStatus resp = {}", response);
        return null;
    }
}
