package com.datum.platform.grpc.client.impl;

import carrier.api.PowerRpcApi;
import carrier.api.PowerServiceGrpc;
import com.datum.platform.common.exception.BusinessException;
import com.datum.platform.grpc.client.GrpcPowerServiceClient;
import com.datum.platform.grpc.constant.GrpcConstant;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class GrpcPowerServiceClientImpl implements GrpcPowerServiceClient {

    @GrpcClient("carrier-grpc-server")
    PowerServiceGrpc.PowerServiceBlockingStub powerServiceStub;

    @Override
    public List<PowerRpcApi.GetGlobalPowerDetail> getGlobalPowerDetailList(Long latestSynced) {
        PowerRpcApi.GetGlobalPowerDetailListRequest request = PowerRpcApi.GetGlobalPowerDetailListRequest.newBuilder()
                .setLastUpdated(latestSynced)
                .setPageSize(GrpcConstant.PAGE_SIZE)
                .build();

        PowerRpcApi.GetGlobalPowerDetailListResponse response = powerServiceStub.getGlobalPowerDetailList(request);
        if (response.getStatus() != GrpcConstant.GRPC_SUCCESS_CODE) {
            log.error("GrpcTaskServiceClientImpl->getTaskDetailList() fail reason:{}", response.getMsg());
            throw new BusinessException(response.getStatus(), response.getMsg());
        }

        return response.getPowersList();
    }
}
