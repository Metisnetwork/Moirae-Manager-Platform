package com.datum.platform.grpc.client;

import com.datum.platform.grpc.service.types.Organization;

import java.util.List;

public interface GrpcAuthServiceClient {
    List<Organization> getIdentityList(Long latestSynced);
}
