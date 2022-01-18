package com.moirae.rosettaflow.grpc.client;

import com.google.protobuf.Empty;
import com.moirae.rosettaflow.common.exception.BusinessException;
import com.moirae.rosettaflow.grpc.constant.GrpcConstant;
import com.moirae.rosettaflow.grpc.identity.dto.NodeIdentityDto;
import com.moirae.rosettaflow.grpc.metadata.req.dto.MetaDataColumnDetailDto;
import com.moirae.rosettaflow.grpc.metadata.req.dto.MetaDataDetailDto;
import com.moirae.rosettaflow.grpc.metadata.req.dto.MetaDataSummaryDto;
import com.moirae.rosettaflow.grpc.metadata.resp.dto.MetaDataDetailResponseDto;
import com.moirae.rosettaflow.grpc.metadata.resp.dto.SelfMetaDataDetailResponseDto;
import com.moirae.rosettaflow.grpc.service.*;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
    MetadataServiceGrpc.MetadataServiceBlockingStub metaDataServiceBlockingStub;

    /**
     * 查看全网元数据列表
     *
     * @return 获取所有元数据列表
     */
    public List<MetaDataDetailResponseDto> getGlobalMetadataDetailList(@NonNull Long latestSynced) {
        List<MetaDataDetailResponseDto> metaDataDetailResponseDtoList = new ArrayList<>();

        GetGlobalMetadataDetailListRequest request = GetGlobalMetadataDetailListRequest.newBuilder()
                .setLastUpdated(latestSynced)
                .setPageSize(GrpcConstant.PAGE_SIZE)
                .build();
        GetGlobalMetadataDetailListResponse globalMetadataDetailList = metaDataServiceBlockingStub.getGlobalMetadataDetailList(request);

        processRespList(metaDataDetailResponseDtoList, globalMetadataDetailList);

        return metaDataDetailResponseDtoList;
    }

    /**
     * 查看 本组织元数据列表
     *
     * @return 本组织元数据列表
     */
    public List<SelfMetaDataDetailResponseDto> getLocalMetadataDetailList(@NonNull Long latestSynced) {
        List<SelfMetaDataDetailResponseDto> selfMetaDataDetailResponseDtoList = new ArrayList<>();

        GetLocalMetadataDetailListRequest request = GetLocalMetadataDetailListRequest.newBuilder()
                .setLastUpdated(latestSynced)
                .setPageSize(GrpcConstant.PAGE_SIZE)
                .build();
        GetLocalMetadataDetailListResponse getSelfMetadataDetailListResponse = metaDataServiceBlockingStub.getLocalMetadataDetailList(request);
        if (getSelfMetadataDetailListResponse.getStatus() != GrpcConstant.GRPC_SUCCESS_CODE) {
            log.error("MetaDataServiceClient->getLocalMetadataDetailList() fail reason:{}", getSelfMetadataDetailListResponse.getMsg());
            throw new BusinessException(getSelfMetadataDetailListResponse.getStatus(), getSelfMetadataDetailListResponse.getMsg());
        }

        SelfMetaDataDetailResponseDto selfMetaDataDetailResponseDto;
        for (int i = 0; i < getSelfMetadataDetailListResponse.getMetadataListCount(); i++) {
            selfMetaDataDetailResponseDto = new SelfMetaDataDetailResponseDto();
            convertToDto(getSelfMetadataDetailListResponse.getMetadataList(i), selfMetaDataDetailResponseDto);
            selfMetaDataDetailResponseDtoList.add(selfMetaDataDetailResponseDto);
        }

        return selfMetaDataDetailResponseDtoList;
    }

    /**
     * 查询某 metadata 参与过的任务的taskId列表
     *
     * @param identityId 组织身份标识
     * @param metadataId 元数据id
     * @return 参与过的任务的taskId列表
     */
    public List<String> getMetadataUsedTaskIdList(String identityId, String metadataId) {
        List<String> taskIdList = new ArrayList<>();

        GetMetadataUsedTaskIdListRequest getMetadataUsedTaskIdListRequest = GetMetadataUsedTaskIdListRequest.newBuilder()
                .setIdentityId(identityId)
                .setMetadataId(metadataId)
                .build();
        GetMetadataUsedTaskIdListResponse getMetadataUsedTaskIdListResponse = metaDataServiceBlockingStub.getMetadataUsedTaskIdList(getMetadataUsedTaskIdListRequest);

        for (int i = 0; i < getMetadataUsedTaskIdListResponse.getTaskIdsCount(); i++) {
            taskIdList.add(getMetadataUsedTaskIdListResponse.getTaskIds(i));
        }
        return taskIdList;
    }

    private void processRespList(List<MetaDataDetailResponseDto> metaDataDetailResponseDtoList, GetGlobalMetadataDetailListResponse metaDataDetailListResponse) {
        if (metaDataDetailListResponse.getStatus() != GrpcConstant.GRPC_SUCCESS_CODE) {
            log.error("MetaDataServiceClient->processRespList() fail reason:{}", metaDataDetailListResponse.getMsg());
            throw new BusinessException(metaDataDetailListResponse.getStatus(), metaDataDetailListResponse.getMsg());
        }

        MetaDataDetailResponseDto metaDataDetailResponseDto;
        for (int i = 0; i < metaDataDetailListResponse.getMetadataListCount(); i++) {
            metaDataDetailResponseDto = new MetaDataDetailResponseDto();
            convertToDto(metaDataDetailListResponse.getMetadataList(i), metaDataDetailResponseDto);
            metaDataDetailResponseDtoList.add(metaDataDetailResponseDto);
        }
    }

    private void convertToDto(GetGlobalMetadataDetailResponse metaDataDetailResponse, MetaDataDetailResponseDto metaDataDetailResponseDto) {
        //元数据的拥有者
        NodeIdentityDto nodeIdentityDto = assemblyOwner(metaDataDetailResponse.getOwner());
        metaDataDetailResponseDto.setOwner(nodeIdentityDto);

        //元数据的详情信息
        MetaDataDetailDto metaDataDetailShowDto = assemblyMetaDataDetailDto(metaDataDetailResponse.getInformation());
        metaDataDetailResponseDto.setMetaDataDetailDto(metaDataDetailShowDto);
    }

    private void convertToDto(GetLocalMetadataDetailResponse metaDataDetailResponse, SelfMetaDataDetailResponseDto selfMetaDataDetailResponseDto) {
        //元数据的拥有者
        NodeIdentityDto nodeIdentityDto = assemblyOwner(metaDataDetailResponse.getOwner());
        selfMetaDataDetailResponseDto.setOwner(nodeIdentityDto);

        //元数据的详情信息
        MetaDataDetailDto metaDataDetailShowDto = assemblyMetaDataDetailDto(metaDataDetailResponse.getInformation());
        selfMetaDataDetailResponseDto.setMetaDataDetailDto(metaDataDetailShowDto);

        //是否为本组织本地元数据 (不对外的元数据, true: 是本组织元数据; false: 不是)
        selfMetaDataDetailResponseDto.setIsLocal(metaDataDetailResponse.getIsInternal());
    }

    private NodeIdentityDto assemblyOwner(Organization owner) {
        NodeIdentityDto nodeIdentityDto = new NodeIdentityDto();
        nodeIdentityDto.setNodeName(owner.getNodeName());
        nodeIdentityDto.setNodeId(owner.getNodeId());
        nodeIdentityDto.setIdentityId(owner.getIdentityId());
        nodeIdentityDto.setStatus(owner.getStatusValue());
        return nodeIdentityDto;
    }

    private MetaDataDetailDto assemblyMetaDataDetailDto(MetadataDetail information) {
        MetaDataDetailDto metaDataDetailShowDto = new MetaDataDetailDto();
        //元数据摘要
        MetaDataSummaryDto metaDataSummaryDto = new MetaDataSummaryDto();
        metaDataSummaryDto.setMetaDataId(information.getMetadataSummary().getMetadataId());
        metaDataSummaryDto.setOriginId(information.getMetadataSummary().getOriginId());
        metaDataSummaryDto.setTableName(information.getMetadataSummary().getTableName());
        metaDataSummaryDto.setDesc(information.getMetadataSummary().getDesc());
        metaDataSummaryDto.setFilePath(information.getMetadataSummary().getFilePath());
        metaDataSummaryDto.setRows(information.getMetadataSummary().getRows());
        metaDataSummaryDto.setColumns(information.getMetadataSummary().getColumns());
        metaDataSummaryDto.setSize(information.getMetadataSummary().getSize());
        metaDataSummaryDto.setFileType(information.getMetadataSummary().getFileTypeValue());
        metaDataSummaryDto.setHasTitle(information.getMetadataSummary().getHasTitle());
        metaDataSummaryDto.setIndustry(information.getMetadataSummary().getIndustry());
        metaDataSummaryDto.setDataState(information.getMetadataSummary().getStateValue());
        metaDataSummaryDto.setPublishAt(information.getMetadataSummary().getPublishAt());
        metaDataSummaryDto.setUpdateAt(information.getMetadataSummary().getUpdateAt());
        metaDataDetailShowDto.setMetaDataSummary(metaDataSummaryDto);
        //元数据对应原始文件对外暴露的列描述列表
        List<MetaDataColumnDetailDto> metaDataColumnDetailDtoList = new ArrayList<>();
        MetaDataColumnDetailDto metaDataColumnDetailDto;
        MetadataColumn metadataColumn;
        for (int i = 0; i < information.getMetadataColumnsCount(); i++) {
            metadataColumn = information.getMetadataColumns(i);
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
        metaDataDetailShowDto.setTotalTaskCount(information.getTotalTaskCount());
        return metaDataDetailShowDto;
    }
}
