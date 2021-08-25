package com.platon.rosettaflow.grpc.service.impl;

import com.platon.rosettaflow.common.enums.UserMetaDataAuditEnum;
import com.platon.rosettaflow.common.enums.UserTypeEnum;
import com.platon.rosettaflow.grpc.client.MetaDataServiceClient;
import com.platon.rosettaflow.grpc.identity.dto.NodeIdentityDto;
import com.platon.rosettaflow.grpc.metadata.req.dto.ApplyMetaDataAuthorityRequestDto;
import com.platon.rosettaflow.grpc.metadata.req.dto.MetaDataAuthorityDto;
import com.platon.rosettaflow.grpc.metadata.req.dto.MetaDataUsageDto;
import com.platon.rosettaflow.grpc.metadata.resp.dto.ApplyMetaDataAuthorityResponseDto;
import com.platon.rosettaflow.grpc.metadata.resp.dto.GetMetaDataAuthorityDto;
import com.platon.rosettaflow.grpc.service.GrpcAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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
        List<GetMetaDataAuthorityDto> metaDataAuthorityDtoList = new ArrayList<>();

        GetMetaDataAuthorityDto getMetaDataAuthorityDto;
        MetaDataAuthorityDto metaDataAuthorityDto;
        NodeIdentityDto nodeIdentityDto;
        MetaDataUsageDto metaDataUsageDto;

        for (int i = 0; i < 23; i++) {
            getMetaDataAuthorityDto = new GetMetaDataAuthorityDto();
            getMetaDataAuthorityDto.setMetaDataAuthId("metaDataAuthId" + i);
            getMetaDataAuthorityDto.setUser("0x123456789" + i);
            getMetaDataAuthorityDto.setUserType(UserTypeEnum.ATP.getValue());

            metaDataAuthorityDto = new MetaDataAuthorityDto();
            nodeIdentityDto = new NodeIdentityDto();
            nodeIdentityDto.setName("name" + i);
            nodeIdentityDto.setNodeId("nodeId" + i);
            nodeIdentityDto.setIdentityId("identityId" + i);
            metaDataAuthorityDto.setOwner(nodeIdentityDto);

            metaDataAuthorityDto.setMetaDataId("metaDataId" + i);

            metaDataUsageDto = new MetaDataUsageDto();
            metaDataUsageDto.setUserType(1);
            metaDataUsageDto.setStartAt(1629877270100L);
            metaDataUsageDto.setEndAt(1629877270100L);
            metaDataUsageDto.setTimes(100L + i);
            metaDataAuthorityDto.setMetaDataUsageDto(metaDataUsageDto);
            getMetaDataAuthorityDto.setMetaDataAuthorityDto(metaDataAuthorityDto);

            getMetaDataAuthorityDto.setAuditMetaDataOption((int) UserMetaDataAuditEnum.AUDIT_PASSED.getValue());
            getMetaDataAuthorityDto.setApplyAt(1629877270100L);
            getMetaDataAuthorityDto.setAuditAt(1629877270100L);
            metaDataAuthorityDtoList.add(getMetaDataAuthorityDto);
        }
        return metaDataAuthorityDtoList;
    }

    @Override
    public NodeIdentityDto getNodeIdentity() {
        return null;
    }
}
