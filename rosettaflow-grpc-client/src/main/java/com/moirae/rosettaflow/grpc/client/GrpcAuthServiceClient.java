package com.moirae.rosettaflow.grpc.client;

import com.moirae.rosettaflow.grpc.service.types.Organization;

import java.util.List;

public interface GrpcAuthServiceClient {
    List<Organization> getIdentityList(Long latestSynced);
}
