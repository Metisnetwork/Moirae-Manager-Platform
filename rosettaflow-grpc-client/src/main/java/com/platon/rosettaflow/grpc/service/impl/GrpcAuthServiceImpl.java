package com.platon.rosettaflow.grpc.service.impl;

import com.platon.rosettaflow.grpc.client.AuthServiceClient;
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
 * @description 功能描述
 */
@Slf4j
@Service
@Profile({"test", "prod"})
public class GrpcAuthServiceImpl implements GrpcAuthService {

    @Resource
    private AuthServiceClient authServiceClient;

    @Override
    public ApplyMetaDataAuthorityResponseDto applyMetaDataAuthority(ApplyMetaDataAuthorityRequestDto requestDto) {
        return authServiceClient.applyMetaDataAuthority(requestDto);
    }

    @Override
    public List<GetMetaDataAuthorityDto> getMetaDataAuthorityList() {
        return authServiceClient.getMetaDataAuthorityList();
    }

    @Override
    public NodeIdentityDto getNodeIdentity() {
        return authServiceClient.getNodeIdentity();
    }
}
