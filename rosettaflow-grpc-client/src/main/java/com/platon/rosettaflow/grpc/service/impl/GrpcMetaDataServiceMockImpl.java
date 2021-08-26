package com.platon.rosettaflow.grpc.service.impl;

import com.platon.rosettaflow.grpc.identity.dto.NodeIdentityDto;
import com.platon.rosettaflow.grpc.metadata.req.dto.MetaDataColumnDetailDto;
import com.platon.rosettaflow.grpc.metadata.req.dto.MetaDataDetailDto;
import com.platon.rosettaflow.grpc.metadata.req.dto.MetaDataDetailResponseDto;
import com.platon.rosettaflow.grpc.metadata.req.dto.MetaDataSummaryDto;
import com.platon.rosettaflow.grpc.service.GrpcMetaDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/24
 * @description 功能描述
 */
@Slf4j
@Service
@Profile({"dev", "local"})
public class GrpcMetaDataServiceMockImpl implements GrpcMetaDataService {

    @Override
    public MetaDataDetailResponseDto getMetaDataDetail(String identityId, String metaDataId) {
        return null;
    }

    @Override
    public List<MetaDataDetailResponseDto> getMetaDataDetailList() {
        List<MetaDataDetailResponseDto> responseDtoList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            MetaDataDetailResponseDto metaData = new MetaDataDetailResponseDto();
            NodeIdentityDto identityDto = new NodeIdentityDto();
            identityDto.setName("hudenian" + i);
            identityDto.setNodeId("nodeId" + i);
            identityDto.setIdentityId("identityId" + i);

            MetaDataDetailDto metaDataDetailDto = new MetaDataDetailDto();
            MetaDataSummaryDto metaDataSummaryDto = new MetaDataSummaryDto();
            metaDataSummaryDto.setMetaDataId("MetaDataId" + i);
            metaDataSummaryDto.setOriginId("OriginId" + i);
            metaDataSummaryDto.setTableName("TableName" + i);
            metaDataSummaryDto.setDesc("desc" + i);
            metaDataSummaryDto.setFilePath("FilePath" + i);
            metaDataSummaryDto.setRows(i);
            metaDataSummaryDto.setColumns(i);
            metaDataSummaryDto.setSize(i);
            metaDataSummaryDto.setFileType("csv");
            metaDataSummaryDto.setHasTitle(true);
            metaDataSummaryDto.setIndustry("计算机");
            metaDataSummaryDto.setState("release");

            metaDataDetailDto.setMetaDataSummary(metaDataSummaryDto);

            List<MetaDataColumnDetailDto> metaDataColumnDetailDtoList = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                MetaDataColumnDetailDto metaDataColumnDetailDto = new MetaDataColumnDetailDto();
                metaDataColumnDetailDto.setIndex(j);
                metaDataColumnDetailDto.setName("name" + j);
                metaDataColumnDetailDto.setType("String");
                metaDataColumnDetailDto.setSize(1231);
                metaDataColumnDetailDto.setComment("列描述" + j);
                metaDataColumnDetailDtoList.add(metaDataColumnDetailDto);
            }
            metaDataDetailDto.setMetaDataColumnDetailDtoList(metaDataColumnDetailDtoList);

            metaData.setOwner(identityDto);
            metaData.setMetaDataDetailDto(metaDataDetailDto);
            responseDtoList.add(metaData);
        }
        return responseDtoList;
    }

    @Override
    public List<MetaDataDetailResponseDto> getMetaDataDetailListByOwner(String identityId) {
        return null;
    }
}
