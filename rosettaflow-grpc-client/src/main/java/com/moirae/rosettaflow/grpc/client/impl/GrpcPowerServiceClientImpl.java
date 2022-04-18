package com.moirae.rosettaflow.grpc.client.impl;

import com.moirae.rosettaflow.common.exception.BusinessException;
import com.moirae.rosettaflow.grpc.client.GrpcPowerServiceClient;
import com.moirae.rosettaflow.grpc.constant.GrpcConstant;
import com.moirae.rosettaflow.grpc.service.GetGlobalPowerDetail;
import com.moirae.rosettaflow.grpc.service.GetGlobalPowerDetailListRequest;
import com.moirae.rosettaflow.grpc.service.GetGlobalPowerDetailListResponse;
import com.moirae.rosettaflow.grpc.service.PowerServiceGrpc;
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
    public List<GetGlobalPowerDetail> getGlobalPowerDetailList(Long latestSynced) {
        GetGlobalPowerDetailListRequest request = GetGlobalPowerDetailListRequest.newBuilder()
                .setLastUpdated(latestSynced)
                .setPageSize(GrpcConstant.PAGE_SIZE)
                .build();

        GetGlobalPowerDetailListResponse response = powerServiceStub.getGlobalPowerDetailList(request);
        if (response.getStatus() != GrpcConstant.GRPC_SUCCESS_CODE) {
            log.error("GrpcTaskServiceClientImpl->getTaskDetailList() fail reason:{}", response.getMsg());
            throw new BusinessException(response.getStatus(), response.getMsg());
        }

        return response.getPowersList();
    }
}
