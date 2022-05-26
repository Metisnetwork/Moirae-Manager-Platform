package com.datum.platform.grpc.client.impl;

import com.google.protobuf.Empty;
import com.datum.platform.common.exception.BusinessException;
import com.datum.platform.grpc.client.GrpcSysServiceClient;
import com.datum.platform.grpc.constant.GrpcConstant;
import com.datum.platform.grpc.service.*;
import io.grpc.Channel;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GrpcSysServiceClientImpl implements GrpcSysServiceClient {

    @GrpcClient("carrier-grpc-server")
    YarnServiceGrpc.YarnServiceBlockingStub yarnServiceBlockingStub;

    @Override
    public YarnNodeInfo getNodeInfo(Channel channel) {
        GetNodeInfoResponse response = YarnServiceGrpc.newBlockingStub(channel).getNodeInfo(Empty.newBuilder().build());
        if (response.getStatus() != GrpcConstant.GRPC_SUCCESS_CODE) {
            log.error("GrpcAuthServiceClientImpl->getIdentityList() fail reason:{}", response.getMsg());
            throw new BusinessException(response.getStatus(), response.getMsg());
        }
        return response.getInformation();
    }

    @Override
    public GetTaskResultFileSummary getTaskResultFileSummary(Channel channel, String taskId) {
        GetTaskResultFileSummaryRequest request = GetTaskResultFileSummaryRequest.newBuilder()
                .setTaskId(taskId)
                .build();

        GetTaskResultFileSummaryResponse response = YarnServiceGrpc.newBlockingStub(channel).getTaskResultFileSummary(request);
        if (response.getStatus() != GrpcConstant.GRPC_SUCCESS_CODE) {
            log.error("GrpcAuthServiceClientImpl->getIdentityList() fail reason:{}", response.getMsg());
            throw new BusinessException(response.getStatus(), response.getMsg());
        }

        return response.getInformation();
    }
}
