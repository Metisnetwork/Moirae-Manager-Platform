package com.moirae.rosettaflow.grpc.client;

import com.moirae.rosettaflow.grpc.service.GetGlobalMetadataDetail;

import java.util.List;

public interface GrpcMetaDataServiceClient {
     List<GetGlobalMetadataDetail> getGlobalMetadataDetailList(Long latestSynced);
}
