package com.platon.rosettaflow.grpc.service.impl;

import com.platon.rosettaflow.grpc.client.MetaDataServiceClient;
import com.platon.rosettaflow.grpc.identity.dto.NodeIdentityDto;
import com.platon.rosettaflow.grpc.metadata.req.dto.ApplyMetaDataAuthorityRequestDto;
import com.platon.rosettaflow.grpc.metadata.resp.dto.ApplyMetaDataAuthorityResponseDto;
import com.platon.rosettaflow.grpc.metadata.resp.dto.GetMetaDataAuthorityDto;
import com.platon.rosettaflow.grpc.service.GrpcAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/24
 * @description 获取元数据mock信息
 */
@Slf4j
@Service
@Profile({"dev", "local"})
public class GrpcAuthServiceMockImpl implements GrpcAuthService {

    @Resource
    private MetaDataServiceClient metaDataServiceClient;

    @Override
    public ApplyMetaDataAuthorityResponseDto applyMetaDataAuthority(ApplyMetaDataAuthorityRequestDto requestDto) {
        ApplyMetaDataAuthorityResponseDto applyMetaDataAuthorityResponseDto = new ApplyMetaDataAuthorityResponseDto();
        return null;
    }

    @Override
    public List<GetMetaDataAuthorityDto> getMetaDataAuthorityList() {
        return null;
    }

    @Override
    public NodeIdentityDto getNodeIdentity() {
        return null;
    }
}
