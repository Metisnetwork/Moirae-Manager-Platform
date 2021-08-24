package com.platon.rosettaflow.grpc.service.impl;

import com.platon.rosettaflow.grpc.client.MetaDataServiceClient;
import com.platon.rosettaflow.grpc.metadata.req.dto.MetaDataDetailResponseDto;
import com.platon.rosettaflow.grpc.service.GrpcMetaDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/24
 * @description 获取真实元数据信息
 */
@Slf4j
@Service
@Profile({"test", "prod"})
public class GrpcMetaDataServiceImpl implements GrpcMetaDataService {

    @Resource
    private MetaDataServiceClient metaDataServiceClient;

    @Override
    public MetaDataDetailResponseDto getMetaDataDetail(String identityId, String metaDataId) {
        return metaDataServiceClient.getMetaDataDetail(identityId, metaDataId);
    }

    @Override
    public List<MetaDataDetailResponseDto> getMetaDataDetailList() {
        return metaDataServiceClient.getMetaDataDetailList();
    }

    @Override
    public List<MetaDataDetailResponseDto> getMetaDataDetailListByOwner(String identityId) {
        return metaDataServiceClient.getMetaDataDetailListByOwner(identityId);
    }
}
