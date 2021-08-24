package com.platon.rosettaflow.grpc.service;

import com.platon.rosettaflow.grpc.metadata.req.dto.MetaDataDetailResponseDto;

import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/24
 * @description 功能描述
 */
public interface GrpcMetaDataService {

    /**
     * 查看单个元数据详情 (包含 列字段描述)
     *
     * @param identityId 组织的身份标识Id
     * @param metaDataId 元数据Id
     * @return 元数据详情响应体
     */
    MetaDataDetailResponseDto getMetaDataDetail(String identityId, String metaDataId);

    /**
     * 查看元数据详情列表
     *
     * @return 获取所有元数据列表
     */
    List<MetaDataDetailResponseDto> getMetaDataDetailList();

    /**
     * 通过表的拥有者（owner）获取元数据详情
     *
     * @return 获取所有元数据列表
     */
    List<MetaDataDetailResponseDto> getMetaDataDetailListByOwner(String identityId);
}
