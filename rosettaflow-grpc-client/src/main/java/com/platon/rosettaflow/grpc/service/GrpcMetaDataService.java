package com.platon.rosettaflow.grpc.service;

import com.platon.rosettaflow.grpc.metadata.resp.dto.MetaDataDetailResponseDto;
import com.platon.rosettaflow.grpc.metadata.resp.dto.SelfMetaDataDetailResponseDto;

import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/24
 * @description 功能描述
 */
public interface GrpcMetaDataService {

    /**
     * 查看全网元数据列表
     *
     * @return 获取所有元数据列表
     */
    List<MetaDataDetailResponseDto> getGlobalMetadataDetailList();

    /**
     * 查看 本组织元数据列表
     *
     * @return 本组织元数据列表
     */
    List<SelfMetaDataDetailResponseDto> getLocalMetadataDetailList();

    /**
     * 查询某 metadata 参与过的任务的taskId列表
     *
     * @param identityId 组织身份标识
     * @param metadataId 元数据id
     * @return 参与过的任务的taskId列表
     */
    List<String> getMetadataUsedTaskIdList(String identityId, String metadataId);

}
