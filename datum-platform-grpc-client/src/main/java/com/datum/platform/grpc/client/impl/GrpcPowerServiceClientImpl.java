package com.datum.platform.grpc.client.impl;

import com.datum.platform.common.exception.BusinessException;
import com.datum.platform.grpc.client.GrpcPowerServiceClient;
import com.datum.platform.grpc.constant.GrpcConstant;
import com.datum.platform.grpc.service.GetGlobalPowerDetail;
import com.datum.platform.grpc.service.GetGlobalPowerDetailListRequest;
import com.datum.platform.grpc.service.GetGlobalPowerDetailListResponse;
import com.datum.platform.grpc.service.PowerServiceGrpc;
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
