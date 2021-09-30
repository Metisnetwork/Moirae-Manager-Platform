package com.platon.rosettaflow.grpc.service;

import com.platon.rosettaflow.grpc.identity.dto.NodeIdentityDto;
import com.platon.rosettaflow.grpc.metadata.req.dto.ApplyMetaDataAuthorityRequestDto;
import com.platon.rosettaflow.grpc.metadata.resp.dto.ApplyMetaDataAuthorityResponseDto;
import com.platon.rosettaflow.grpc.metadata.resp.dto.GetMetaDataAuthorityDto;

import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/24
 * @description 功能描述
 */
public interface GrpcAuthService {
    /**
     * 元数据授权申请
     *
     * @param requestDto 授权申请信息
     * @return 申请结果
     */
    ApplyMetaDataAuthorityResponseDto applyMetaDataAuthority(ApplyMetaDataAuthorityRequestDto requestDto);

    /**
     * 查询(本组织)的所有元数据的授权申请及审核结果详情列表
     *
     * @return 元数据列表
     */
    List<GetMetaDataAuthorityDto> getMetaDataAuthorityList();

    /**
     * 查询自己组织信息
     *
     * @return 组织信息
     */
    NodeIdentityDto getNodeIdentity();

    /**
     * 查询全网组织的身份信息列表(已入网的)
     *
     * @return 全网组织的身份信息列表
     */
    List<NodeIdentityDto> getIdentityList();

    /**
     * 查询(全网)的所有元数据的授权申请及审核结果详情列表
     *
     * @return 授权申请及审核结果详情列表
     */
    List<GetMetaDataAuthorityDto> getMetadataAuthorityListByUser();
}
