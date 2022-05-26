package com.datum.platform.grpc.client.impl;

import com.datum.platform.common.exception.BusinessException;
import com.datum.platform.grpc.client.GrpcAuthServiceClient;
import com.datum.platform.grpc.constant.GrpcConstant;
import com.datum.platform.grpc.service.AuthServiceGrpc;
import com.datum.platform.grpc.service.GetIdentityListRequest;
import com.datum.platform.grpc.service.GetIdentityListResponse;
import com.datum.platform.grpc.service.types.Organization;
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
