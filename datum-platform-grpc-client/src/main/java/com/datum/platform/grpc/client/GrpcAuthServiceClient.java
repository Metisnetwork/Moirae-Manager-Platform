package com.datum.platform.grpc.client;

import carrier.types.Identitydata;

import java.util.List;

public interface GrpcAuthServiceClient {
    List<Identitydata.Organization> getIdentityList(Long latestSynced);
}
