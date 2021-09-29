package com.platon.rosettaflow.grpc.service.impl;

import com.platon.rosettaflow.grpc.identity.dto.NodeIdentityDto;
import com.platon.rosettaflow.grpc.metadata.req.dto.MetaDataColumnDetailDto;
import com.platon.rosettaflow.grpc.metadata.req.dto.MetaDataDetailDto;
import com.platon.rosettaflow.grpc.metadata.req.dto.MetaDataSummaryDto;
import com.platon.rosettaflow.grpc.metadata.resp.dto.MetaDataDetailResponseDto;
import com.platon.rosettaflow.grpc.metadata.resp.dto.SelfMetaDataDetailResponseDto;
import com.platon.rosettaflow.grpc.service.GrpcMetaDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/24
 * @description 获取真实元数据信息mocker类
 */
@Slf4j
@Service
@Profile({"dev"})
public class GrpcMetaDataServiceMockImpl implements GrpcMetaDataService {

    /**
     * 数据记录记录数
     */
    static final int LEN = 10;

    /**
     * 数据明细记录记录数
     */
    static final int LEN_DETAIL = 5;

    @Override
    public List<MetaDataDetailResponseDto> getGlobalMetadataDetailList() {
        List<MetaDataDetailResponseDto> responseDtoList = new ArrayList<>();
        for (int i = 0; i < LEN; i++) {
            MetaDataDetailResponseDto metaData = new MetaDataDetailResponseDto();
            NodeIdentityDto identityDto = new NodeIdentityDto();
            identityDto.setNodeName("hudenian" + i);
            identityDto.setNodeId("nodeId" + i);
            identityDto.setIdentityId("identityId" + i);

            MetaDataDetailDto metaDataDetailDto = new MetaDataDetailDto();
            MetaDataSummaryDto metaDataSummaryDto = new MetaDataSummaryDto();
            metaDataSummaryDto.setMetaDataId("MetaDataId" + i);
            metaDataSummaryDto.setOriginId("OriginId" + i);
            metaDataSummaryDto.setTableName("TableName" + i);
            metaDataSummaryDto.setDesc("这是mock造的假数据" + i);
            metaDataSummaryDto.setFilePath("FilePath" + i);

            metaDataSummaryDto.setColumns(i);
            metaDataSummaryDto.setSize((long) i + 1000);
            metaDataSummaryDto.setFileType(1);
            metaDataSummaryDto.setHasTitle(true);
            metaDataSummaryDto.setIndustry("计算机");
            metaDataSummaryDto.setDataState(2);

            metaDataDetailDto.setMetaDataSummary(metaDataSummaryDto);

            List<MetaDataColumnDetailDto> metaDataColumnDetailDtoList = new ArrayList<>();
            for (int j = 0; j < LEN_DETAIL; j++) {
                MetaDataColumnDetailDto metaDataColumnDetailDto = new MetaDataColumnDetailDto();
                metaDataColumnDetailDto.setIndex(j);
                metaDataColumnDetailDto.setName("name" + j);
                metaDataColumnDetailDto.setType("String");
                metaDataColumnDetailDto.setSize(1231);
                metaDataColumnDetailDto.setComment("列描述" + j);
                metaDataColumnDetailDtoList.add(metaDataColumnDetailDto);
            }
            metaDataDetailDto.setMetaDataColumnDetailDtoList(metaDataColumnDetailDtoList);

            metaDataSummaryDto.setRows(metaDataColumnDetailDtoList.size());

            metaData.setOwner(identityDto);
            metaData.setMetaDataDetailDto(metaDataDetailDto);
            responseDtoList.add(metaData);
        }
        return responseDtoList;
    }

    @Override
    public List<SelfMetaDataDetailResponseDto> getLocalMetadataDetailList() {
        return null;
    }

    @Override
    public List<String> getMetadataUsedTaskIdList(String identityId, String metadataId) {
        return null;
    }

}
