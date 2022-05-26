package com.datum.platform.grpc.client;

import com.datum.platform.grpc.service.GetGlobalPowerDetail;

import java.util.List;

public interface GrpcPowerServiceClient {
    List<GetGlobalPowerDetail> getGlobalPowerDetailList(Long latestSynced);
}
