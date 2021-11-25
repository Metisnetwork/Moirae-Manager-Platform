package com.moirae.rosettaflow.grpc.service.impl;

import com.moirae.rosettaflow.grpc.client.AuthServiceClient;
import com.moirae.rosettaflow.grpc.identity.dto.NodeIdentityDto;
import com.moirae.rosettaflow.grpc.metadata.req.dto.ApplyMetaDataAuthorityRequestDto;
import com.moirae.rosettaflow.grpc.metadata.req.dto.RevokeMetaDataAuthorityRequestDto;
import com.moirae.rosettaflow.grpc.metadata.resp.dto.ApplyMetaDataAuthorityResponseDto;
import com.moirae.rosettaflow.grpc.metadata.resp.dto.GetMetaDataAuthorityDto;
import com.moirae.rosettaflow.grpc.metadata.resp.dto.RevokeMetadataAuthorityResponseDto;
import com.moirae.rosettaflow.grpc.service.GrpcAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/24
 * @description 功能描述
 */
@Slf4j
@Service
@Profile({"prod", "test", "local", "xty"})
public class GrpcAuthServiceImpl implements GrpcAuthService {

    @Resource
    private AuthServiceClient authServiceClient;

    @Override
    public ApplyMetaDataAuthorityResponseDto applyMetaDataAuthority(ApplyMetaDataAuthorityRequestDto requestDto) {
        return authServiceClient.applyMetaDataAuthority(requestDto);
    }

    @Override
    public RevokeMetadataAuthorityResponseDto revokeMetadataAuthority(RevokeMetaDataAuthorityRequestDto requestDto) {
        return authServiceClient.revokeMetadataAuthority(requestDto);
    }

    @Override
    public List<GetMetaDataAuthorityDto> getMetaDataAuthorityList() {
        return authServiceClient.getMetaDataAuthorityList();
    }

    @Override
    public NodeIdentityDto getNodeIdentity() {
        return authServiceClient.getNodeIdentity();
    }

    @Override
    public List<NodeIdentityDto> getIdentityList() {
        return authServiceClient.getIdentityList();
    }

    @Override
    public List<GetMetaDataAuthorityDto> getGlobalMetadataAuthorityList() {
        return authServiceClient.getGlobalMetadataAuthorityList();
    }

}
