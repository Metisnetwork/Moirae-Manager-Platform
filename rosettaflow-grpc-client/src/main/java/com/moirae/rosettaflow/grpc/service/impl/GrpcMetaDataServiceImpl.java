package com.moirae.rosettaflow.grpc.service.impl;

import com.moirae.rosettaflow.grpc.client.MetaDataServiceClient;
import com.moirae.rosettaflow.grpc.constant.GrpcConstant;
import com.moirae.rosettaflow.grpc.identity.dto.NodeIdentityDto;
import com.moirae.rosettaflow.grpc.metadata.resp.dto.MetaDataDetailResponseDto;
import com.moirae.rosettaflow.grpc.metadata.resp.dto.SelfMetaDataDetailResponseDto;
import com.moirae.rosettaflow.grpc.service.GrpcMetaDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/24
 * @description 获取真实元数据信息
 */
@Slf4j
@Service
@Profile({"prod", "test", "local", "xty"})
public class GrpcMetaDataServiceImpl implements GrpcMetaDataService {

    @Resource
    private MetaDataServiceClient metaDataServiceClient;

    @Override
    public List<MetaDataDetailResponseDto> getAllGlobalMetadataDetailList() {
        long latestSynced = 0;
        List<MetaDataDetailResponseDto> allList = new ArrayList<>();
        List<MetaDataDetailResponseDto> list;
        do {
            list = metaDataServiceClient.getGlobalMetadataDetailList(latestSynced);
            if(list.isEmpty()){
                break;
            }
            allList.addAll(list);
            latestSynced = list.get(list.size() - 1).getMetaDataDetailDto().getMetaDataSummary().getUpdateAt();
        } while (list.size() == GrpcConstant.PAGE_SIZE);//如果小于pageSize说明是最后一批了
        return allList;
    }

    @Override
    public List<MetaDataDetailResponseDto> getGlobalMetadataDetailList(Long latestSynced) {
        return metaDataServiceClient.getGlobalMetadataDetailList(latestSynced);
    }

    @Override
    public List<SelfMetaDataDetailResponseDto> getAllLocalMetadataDetailList() {
        long latestSynced = 0;
        List<SelfMetaDataDetailResponseDto> allList = new ArrayList<>();
        List<SelfMetaDataDetailResponseDto> list;
        do {
            list = metaDataServiceClient.getLocalMetadataDetailList(latestSynced);
            if(list.isEmpty()){
                break;
            }
            allList.addAll(list);
            latestSynced = list.get(list.size() - 1).getMetaDataDetailDto().getMetaDataSummary().getUpdateAt();
        } while (list.size() == GrpcConstant.PAGE_SIZE);//如果小于pageSize说明是最后一批了
        return allList;
    }
    @Override
    public List<SelfMetaDataDetailResponseDto> getLocalMetadataDetailList(Long latestSynced) {
        return metaDataServiceClient.getLocalMetadataDetailList(latestSynced);
    }

    @Override
    public List<String> getMetadataUsedTaskIdList(String identityId, String metadataId) {
        return metaDataServiceClient.getMetadataUsedTaskIdList(identityId, metadataId);
    }

}
