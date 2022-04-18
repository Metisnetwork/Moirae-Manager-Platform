package com.moirae.rosettaflow.grpc.client.impl;

import com.moirae.rosettaflow.common.exception.BusinessException;
import com.moirae.rosettaflow.grpc.client.GrpcMetaDataServiceClient;
import com.moirae.rosettaflow.grpc.constant.GrpcConstant;
import com.moirae.rosettaflow.grpc.service.GetGlobalMetadataDetail;
import com.moirae.rosettaflow.grpc.service.GetGlobalMetadataDetailListRequest;
import com.moirae.rosettaflow.grpc.service.GetGlobalMetadataDetailListResponse;
import com.moirae.rosettaflow.grpc.service.MetadataServiceGrpc;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author admin
 * @date 2021/9/1
 * @description 元数据相关接口
 */
@Slf4j
@Component
public class GrpcMetaDataServiceClientImpl implements GrpcMetaDataServiceClient {

    @GrpcClient("carrier-grpc-server")
    MetadataServiceGrpc.MetadataServiceBlockingStub metaDataServiceBlockingStub;

    /**
     * 查看全网元数据列表
     *
     * @return 获取所有元数据列表
     */
    public List<GetGlobalMetadataDetail> getGlobalMetadataDetailList(@NonNull Long latestSynced) {

        GetGlobalMetadataDetailListRequest request = GetGlobalMetadataDetailListRequest.newBuilder()
                .setLastUpdated(latestSynced)
                .setPageSize(GrpcConstant.PAGE_SIZE)
                .build();
        GetGlobalMetadataDetailListResponse globalMetadataDetailList = metaDataServiceBlockingStub.getGlobalMetadataDetailList(request);


        if (globalMetadataDetailList.getStatus() != GrpcConstant.GRPC_SUCCESS_CODE) {
            log.error("GrpcAuthServiceClientImpl->getIdentityList() fail reason:{}", globalMetadataDetailList.getMsg());
            throw new BusinessException(globalMetadataDetailList.getStatus(), globalMetadataDetailList.getMsg());
        }
        return  globalMetadataDetailList.getMetadatasList();
    }
}
