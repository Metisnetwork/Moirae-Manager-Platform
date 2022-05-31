package com.datum.platform.grpc.client;

import carrier.api.PowerRpcApi;

import java.util.List;

public interface GrpcPowerServiceClient {
    List<PowerRpcApi.GetGlobalPowerDetail> getGlobalPowerDetailList(Long latestSynced);
}
