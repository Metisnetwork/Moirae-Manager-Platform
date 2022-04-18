package com.moirae.rosettaflow.grpc.client;

import com.moirae.rosettaflow.grpc.service.GetGlobalPowerDetail;

import java.util.List;

public interface GrpcPowerServiceClient {
    List<GetGlobalPowerDetail> getGlobalPowerDetailList(Long latestSynced);
}
