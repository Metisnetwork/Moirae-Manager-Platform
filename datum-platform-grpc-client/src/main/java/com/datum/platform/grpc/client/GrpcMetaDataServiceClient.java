package com.datum.platform.grpc.client;

import carrier.api.MetadataRpcApi;

import java.util.List;

public interface GrpcMetaDataServiceClient {
     List<MetadataRpcApi.GetGlobalMetadataDetail> getGlobalMetadataDetailList(Long latestSynced);
}
