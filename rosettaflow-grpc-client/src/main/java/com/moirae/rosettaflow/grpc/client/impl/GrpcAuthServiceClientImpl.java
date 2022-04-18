package com.moirae.rosettaflow.grpc.client.impl;

import com.moirae.rosettaflow.common.exception.BusinessException;
import com.moirae.rosettaflow.grpc.client.GrpcAuthServiceClient;
import com.moirae.rosettaflow.grpc.constant.GrpcConstant;
import com.moirae.rosettaflow.grpc.service.AuthServiceGrpc;
import com.moirae.rosettaflow.grpc.service.GetIdentityListRequest;
import com.moirae.rosettaflow.grpc.service.GetIdentityListResponse;
import com.moirae.rosettaflow.grpc.service.types.Organization;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class GrpcAuthServiceClientImpl implements GrpcAuthServiceClient {

    @GrpcClient("carrier-grpc-server")
    AuthServiceGrpc.AuthServiceBlockingStub authServiceBlockingStub;

    @Override
    public List<Organization> getIdentityList(Long latestSynced) {
        GetIdentityListRequest request = GetIdentityListRequest.newBuilder()
                .setLastUpdated(latestSynced)
                .setPageSize(GrpcConstant.PAGE_SIZE)
                .build();
        GetIdentityListResponse getIdentityListResponse = authServiceBlockingStub.getIdentityList(request);

        if (getIdentityListResponse.getStatus() != GrpcConstant.GRPC_SUCCESS_CODE) {
            log.error("GrpcAuthServiceClientImpl->getIdentityList() fail reason:{}", getIdentityListResponse.getMsg());
            throw new BusinessException(getIdentityListResponse.getStatus(), getIdentityListResponse.getMsg());
        }
        return  getIdentityListResponse.getIdentitysList();
    }
}
