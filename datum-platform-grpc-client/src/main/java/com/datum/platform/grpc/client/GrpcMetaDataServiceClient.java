package com.datum.platform.grpc.client;

import com.datum.platform.grpc.service.GetGlobalMetadataDetail;

import java.util.List;

public interface GrpcMetaDataServiceClient {
     List<GetGlobalMetadataDetail> getGlobalMetadataDetailList(Long latestSynced);
}
