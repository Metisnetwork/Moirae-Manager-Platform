package com.platon.rosettaflow.grpc.client;

import com.platon.rosettaflow.common.exception.BusinessException;
import com.platon.rosettaflow.grpc.constant.GrpcConstant;
import com.platon.rosettaflow.grpc.identity.dto.NodeIdentityDto;
import com.platon.rosettaflow.grpc.metadata.req.dto.MetaDataColumnDetailDto;
import com.platon.rosettaflow.grpc.metadata.req.dto.MetaDataDetailResponseDto;
import com.platon.rosettaflow.grpc.metadata.req.dto.MetaDataDetailDto;
import com.platon.rosettaflow.grpc.metadata.req.dto.MetaDataSummaryDto;
import com.platon.rosettaflow.grpc.service.*;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 * @date 2021/8/10
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
                .setMetaDataId(metaDataId)
                .build();
        GetMetaDataDetailResponse metaDataDetailResponse = metaDataServiceBlockingStub.getMetaDataDetail(getMetaDataDetailRequest);

        convertToDto(metaDataDetailResponse, metaDataDetailResponseDto);

        return metaDataDetailResponseDto;
    }

    /**
     * 查看元数据详情列表
     *
     * @return 获取所有元数据列表
     */
    public List<MetaDataDetailResponseDto> getMetaDataDetailList() {
        List<MetaDataDetailResponseDto> metaDataDetailResponseDtoList = new ArrayList<>();

        CommonMessage.EmptyGetParams emptyGetParams = CommonMessage.EmptyGetParams.newBuilder().build();
        GetMetaDataDetailListResponse metaDataDetailList = metaDataServiceBlockingStub.getMetaDataDetailList(emptyGetParams);

        processRespList(metaDataDetailResponseDtoList, metaDataDetailList);

        return metaDataDetailResponseDtoList;
    }

    /**
     * 通过表的拥有者（owner）获取元数据详情
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
        nodeIdentityDto.setName(metaDataDetailResponse.getOwner().getName());
        nodeIdentityDto.setNodeId(metaDataDetailResponse.getOwner().getNodeId());
        nodeIdentityDto.setIdentityId(metaDataDetailResponse.getOwner().getIdentityId());

        metaDataDetailResponseDto.setOwner(nodeIdentityDto);

        MetaDataDetailDto metaDataDetailShowDto = new MetaDataDetailDto();
        //元文件详情主体
        MetaDataSummaryDto metaDataSummaryDto = new MetaDataSummaryDto();
        metaDataSummaryDto.setMetaDataId(metaDataDetailResponse.getInformation().getMetaDataSummary().getMetaDataId());
        metaDataSummaryDto.setOriginId(metaDataDetailResponse.getInformation().getMetaDataSummary().getOriginId());
        metaDataSummaryDto.setTableName(metaDataDetailResponse.getInformation().getMetaDataSummary().getTableName());
        metaDataSummaryDto.setDesc(metaDataDetailResponse.getInformation().getMetaDataSummary().getDesc());
        metaDataSummaryDto.setFilePath(metaDataDetailResponse.getInformation().getMetaDataSummary().getFilePath());
        metaDataSummaryDto.setRows(metaDataDetailResponse.getInformation().getMetaDataSummary().getRows());
        metaDataSummaryDto.setColumns(metaDataDetailResponse.getInformation().getMetaDataSummary().getColumns());
        metaDataSummaryDto.setSize(metaDataDetailResponse.getInformation().getMetaDataSummary().getSize());
        metaDataSummaryDto.setFileType(metaDataDetailResponse.getInformation().getMetaDataSummary().getFileType());
        metaDataSummaryDto.setHasTitle(metaDataDetailResponse.getInformation().getMetaDataSummary().getHasTitle());
        metaDataSummaryDto.setState(metaDataDetailResponse.getInformation().getMetaDataSummary().getState());
        metaDataDetailShowDto.setMetaDataSummary(metaDataSummaryDto);
        //元数据列详情
        List<MetaDataColumnDetailDto> metaDataColumnDetailDtoList = new ArrayList<>();
        MetaDataColumnDetailDto metaDataColumnDetailDto;
        MetaDataColumnDetail metaDataColumnDetail;
        for (int i = 0; i < metaDataDetailResponse.getInformation().getColumnMetaList().size(); i++) {
            metaDataColumnDetail = metaDataDetailResponse.getInformation().getColumnMetaList().get(i);
            metaDataColumnDetailDto = new MetaDataColumnDetailDto();
            metaDataColumnDetailDto.setIndex(metaDataColumnDetail.getCindex());
            metaDataColumnDetailDto.setName(metaDataColumnDetail.getCname());
            metaDataColumnDetailDto.setType(metaDataColumnDetail.getCtype());
            metaDataColumnDetailDto.setSize(metaDataColumnDetail.getCsize());
            metaDataColumnDetailDto.setComment(metaDataColumnDetail.getCcomment());
            metaDataColumnDetailDtoList.add(metaDataColumnDetailDto);
        }
        metaDataDetailShowDto.setMetaDataColumnDetailDtoList(metaDataColumnDetailDtoList);

        metaDataDetailResponseDto.setMetaDataDetailDto(metaDataDetailShowDto);
    }

}
