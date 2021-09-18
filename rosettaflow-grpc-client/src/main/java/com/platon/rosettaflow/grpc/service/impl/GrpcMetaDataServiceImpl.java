package com.platon.rosettaflow.grpc.service.impl;

import com.platon.rosettaflow.grpc.client.MetaDataServiceClient;
import com.platon.rosettaflow.grpc.metadata.resp.dto.MetaDataDetailResponseDto;
import com.platon.rosettaflow.grpc.metadata.resp.dto.SelfMetaDataDetailResponseDto;
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
    public List<MetaDataDetailResponseDto> getMetaDataDetailList() {
        return metaDataServiceClient.getMetaDataDetailList();
    }

    @Override
    public List<SelfMetaDataDetailResponseDto> getSelfMetadataDetailList() {
        return metaDataServiceClient.getSelfMetadataDetailList();
    }

    @Override
    public List<String> getMetadataUsedTaskIdList(String identityId, String metadataId) {
        return metaDataServiceClient.getMetadataUsedTaskIdList(identityId, metadataId);
    }

}
