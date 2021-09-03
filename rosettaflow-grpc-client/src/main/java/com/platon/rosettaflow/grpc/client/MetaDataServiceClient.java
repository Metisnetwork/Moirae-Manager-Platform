package com.platon.rosettaflow.grpc.client;

import com.google.protobuf.Empty;
import com.platon.rosettaflow.common.exception.BusinessException;
import com.platon.rosettaflow.grpc.constant.GrpcConstant;
import com.platon.rosettaflow.grpc.identity.dto.NodeIdentityDto;
import com.platon.rosettaflow.grpc.metadata.req.dto.MetaDataColumnDetailDto;
import com.platon.rosettaflow.grpc.metadata.req.dto.MetaDataDetailDto;
import com.platon.rosettaflow.grpc.metadata.req.dto.MetaDataDetailResponseDto;
import com.platon.rosettaflow.grpc.metadata.req.dto.MetaDataSummaryDto;
import com.platon.rosettaflow.grpc.service.*;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 * @date 2021/9/1
 * @description 元数据相关接口
 */
@Slf4j
@Component
public class MetaDataServiceClient {

    @GrpcClient("carrier-grpc-server")
    private MetaDataServiceGrpc.MetaDataServiceBlockingStub metaDataServiceBlockingStub;

    /**
     * 查看单个元数据详情 (包含 列字段描述)
     *
     * @param identityId 组织的身份标识Id
     * @param metaDataId 元数据Id
     * @return 元数据详情响应体
     */
    public MetaDataDetailResponseDto getMetaDataDetail(String identityId, String metaDataId) {

        MetaDataDetailResponseDto metaDataDetailResponseDto = new MetaDataDetailResponseDto();

        GetMetaDataDetailRequest getMetaDataDetailRequest = GetMetaDataDetailRequest.newBuilder()
                .setIdentityId(identityId)
                .setMetadataId(metaDataId)
                .build();
        GetMetaDataDetailResponse metaDataDetailResponse = metaDataServiceBlockingStub.getMetaDataDetail(getMetaDataDetailRequest);

        convertToDto(metaDataDetailResponse, metaDataDetailResponseDto);
        return metaDataDetailResponseDto;
    }

    /**
     * 查看全网元数据列表
     *
     * @return 获取所有元数据列表
     */
    public List<MetaDataDetailResponseDto> getMetaDataDetailList() {
        List<MetaDataDetailResponseDto> metaDataDetailResponseDtoList = new ArrayList<>();

        Empty empty = Empty.newBuilder().build();
        GetMetaDataDetailListResponse metaDataDetailList = metaDataServiceBlockingStub.getMetaDataDetailList(empty);

        processRespList(metaDataDetailResponseDtoList, metaDataDetailList);

        return metaDataDetailResponseDtoList;
    }

    /**
     * 查看某个组织元数据列表
     *
     * @return 获取所有元数据列表
     */
    public List<MetaDataDetailResponseDto> getMetaDataDetailListByOwner(String identityId) {
        List<MetaDataDetailResponseDto> metaDataDetailResponseDtoList = new ArrayList<>();

        GetMetaDataDetailListByOwnerRequest getMetaDataDetailListByOwnerRequest = GetMetaDataDetailListByOwnerRequest.newBuilder()
                .setIdentityId(identityId)
                .build();
        GetMetaDataDetailListResponse metaDataDetailListResponse = metaDataServiceBlockingStub.getMetaDataDetailListByOwner(getMetaDataDetailListByOwnerRequest);
        processRespList(metaDataDetailResponseDtoList, metaDataDetailListResponse);
        return metaDataDetailResponseDtoList;
    }

    private void processRespList(List<MetaDataDetailResponseDto> metaDataDetailResponseDtoList, GetMetaDataDetailListResponse metaDataDetailListResponse) {
        if (metaDataDetailListResponse.getStatus() != GrpcConstant.GRPC_SUCCESS_CODE) {
            throw new BusinessException(metaDataDetailListResponse.getStatus(), metaDataDetailListResponse.getMsg());
        }

        MetaDataDetailResponseDto metaDataDetailResponseDto;
        for (int i = 0; i < metaDataDetailListResponse.getMetaDataListCount(); i++) {
            metaDataDetailResponseDto = new MetaDataDetailResponseDto();
            convertToDto(metaDataDetailListResponse.getMetaDataList(i), metaDataDetailResponseDto);
            metaDataDetailResponseDtoList.add(metaDataDetailResponseDto);
        }
    }

    private void convertToDto(GetMetaDataDetailResponse metaDataDetailResponse, MetaDataDetailResponseDto metaDataDetailResponseDto) {
        //元数据的拥有者
        NodeIdentityDto nodeIdentityDto = new NodeIdentityDto();
        nodeIdentityDto.setNodeName(metaDataDetailResponse.getOwner().getNodeName());
        nodeIdentityDto.setNodeId(metaDataDetailResponse.getOwner().getNodeId());
        nodeIdentityDto.setIdentityId(metaDataDetailResponse.getOwner().getIdentityId());
        metaDataDetailResponseDto.setOwner(nodeIdentityDto);

        //元数据的详情信息
        MetaDataDetailDto metaDataDetailShowDto = new MetaDataDetailDto();
        //元数据摘要
        MetaDataSummaryDto metaDataSummaryDto = new MetaDataSummaryDto();
        metaDataSummaryDto.setMetaDataId(metaDataDetailResponse.getInformation().getMetaDataSummary().getMetaDataId());
        metaDataSummaryDto.setOriginId(metaDataDetailResponse.getInformation().getMetaDataSummary().getOriginId());
        metaDataSummaryDto.setTableName(metaDataDetailResponse.getInformation().getMetaDataSummary().getTableName());
        metaDataSummaryDto.setDesc(metaDataDetailResponse.getInformation().getMetaDataSummary().getDesc());
        metaDataSummaryDto.setFilePath(metaDataDetailResponse.getInformation().getMetaDataSummary().getFilePath());
        metaDataSummaryDto.setRows(metaDataDetailResponse.getInformation().getMetaDataSummary().getRows());
        metaDataSummaryDto.setColumns(metaDataDetailResponse.getInformation().getMetaDataSummary().getColumns());
        metaDataSummaryDto.setSize(metaDataDetailResponse.getInformation().getMetaDataSummary().getSize());
        metaDataSummaryDto.setFileType(metaDataDetailResponse.getInformation().getMetaDataSummary().getFileTypeValue());
        metaDataSummaryDto.setHasTitle(metaDataDetailResponse.getInformation().getMetaDataSummary().getHasTitle());
        metaDataSummaryDto.setIndustry(metaDataDetailResponse.getInformation().getMetaDataSummary().getIndustry());
        metaDataSummaryDto.setState(metaDataDetailResponse.getInformation().getMetaDataSummary().getStateValue());
        metaDataDetailShowDto.setMetaDataSummary(metaDataSummaryDto);
        //元数据对应原始文件对外暴露的列描述列表
        List<MetaDataColumnDetailDto> metaDataColumnDetailDtoList = new ArrayList<>();
        MetaDataColumnDetailDto metaDataColumnDetailDto;
        MetadataColumn metadataColumn;
        for (int i = 0; i < metaDataDetailResponse.getInformation().getMetadataColumnsCount(); i++) {
            metadataColumn = metaDataDetailResponse.getInformation().getMetadataColumns(i);
            metaDataColumnDetailDto = new MetaDataColumnDetailDto();
            metaDataColumnDetailDto.setIndex(metadataColumn.getCIndex());
            metaDataColumnDetailDto.setName(metadataColumn.getCName());
            metaDataColumnDetailDto.setType(metadataColumn.getCType());
            metaDataColumnDetailDto.setSize(metadataColumn.getCSize());
            metaDataColumnDetailDto.setComment(metadataColumn.getCComment());
            metaDataColumnDetailDtoList.add(metaDataColumnDetailDto);
        }
        metaDataDetailShowDto.setMetaDataColumnDetailDtoList(metaDataColumnDetailDtoList);
        //该元数据参与过得任务数 (已完成的和正在执行的)
        metaDataDetailShowDto.setTotalTaskCount(metaDataDetailResponse.getInformation().getTotalTaskCount());
        metaDataDetailResponseDto.setMetaDataDetailDto(metaDataDetailShowDto);
    }
}
