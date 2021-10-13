package com.platon.rosettaflow.grpc.service.impl;

import com.platon.rosettaflow.common.enums.MetaDataUsageEnum;
import com.platon.rosettaflow.common.enums.UserMetaDataAuditEnum;
import com.platon.rosettaflow.common.enums.UserMetaDataAuthorithStateEnum;
import com.platon.rosettaflow.common.enums.UserTypeEnum;
import com.platon.rosettaflow.grpc.constant.GrpcConstant;
import com.platon.rosettaflow.grpc.identity.dto.NodeIdentityDto;
import com.platon.rosettaflow.grpc.metadata.req.dto.ApplyMetaDataAuthorityRequestDto;
import com.platon.rosettaflow.grpc.metadata.req.dto.MetaDataAuthorityDto;
import com.platon.rosettaflow.grpc.metadata.req.dto.MetaDataUsageRuleDto;
import com.platon.rosettaflow.grpc.metadata.resp.dto.ApplyMetaDataAuthorityResponseDto;
import com.platon.rosettaflow.grpc.metadata.resp.dto.GetMetaDataAuthorityDto;
import com.platon.rosettaflow.grpc.metadata.resp.dto.MetadataUsedQuoDto;
import com.platon.rosettaflow.grpc.service.GrpcAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/24
 * @description 获取元数据mock信息
 */
@Slf4j
@Service
@Profile({"dev"})
public class GrpcAuthServiceMockImpl implements GrpcAuthService {
    /**
     * 数据记录记录数
     */
    static final int LEN = 10;

    @Override
    public ApplyMetaDataAuthorityResponseDto applyMetaDataAuthority(ApplyMetaDataAuthorityRequestDto requestDto) {
        ApplyMetaDataAuthorityResponseDto applyMetaDataAuthorityResponseDto = new ApplyMetaDataAuthorityResponseDto();
        applyMetaDataAuthorityResponseDto.setStatus(GrpcConstant.GRPC_SUCCESS_CODE);
        applyMetaDataAuthorityResponseDto.setMsg(GrpcConstant.OK);
        applyMetaDataAuthorityResponseDto.setMetaDataAuthId("testAuthId");
        return applyMetaDataAuthorityResponseDto;
    }

    @Override
    public List<GetMetaDataAuthorityDto> getMetaDataAuthorityList() {
        List<GetMetaDataAuthorityDto> metaDataAuthorityDtoList = new ArrayList<>();

        GetMetaDataAuthorityDto getMetaDataAuthorityDto;
        MetaDataAuthorityDto metaDataAuthorityDto;
        NodeIdentityDto nodeIdentityDto;
        MetaDataUsageRuleDto metaDataUsageDto;

        for (int i = 0; i < LEN; i++) {
            getMetaDataAuthorityDto = new GetMetaDataAuthorityDto();
            getMetaDataAuthorityDto.setMetaDataAuthId("MetaDataId" + i);
            if (i % 2 == 0) {
                getMetaDataAuthorityDto.setUser("0x5146e07c5157ea8ad00bb0c196e61671f4af85f0");
            } else {
                getMetaDataAuthorityDto.setUser("0x007a85e3230b5d96ab15e8e37c7a27daae53e703");
            }

            getMetaDataAuthorityDto.setUserType(UserTypeEnum.ALAYA.getValue());

            metaDataAuthorityDto = new MetaDataAuthorityDto();
            nodeIdentityDto = new NodeIdentityDto();
            nodeIdentityDto.setNodeName("节点" + i + "名字");
            nodeIdentityDto.setNodeId("节点" + i + "的Id");
            nodeIdentityDto.setIdentityId("节点" + i + "的identityId");
            nodeIdentityDto.setStatus(1);
            metaDataAuthorityDto.setOwner(nodeIdentityDto);

            metaDataAuthorityDto.setMetaDataId("metaDataId" + i);

            metaDataUsageDto = new MetaDataUsageRuleDto();
            metaDataUsageDto.setUseType(MetaDataUsageEnum.TIMES.getValue());
            metaDataUsageDto.setStartAt(1629877270100L);
            metaDataUsageDto.setEndAt(1629877270100L);
            metaDataUsageDto.setTimes(100 + i);
            metaDataAuthorityDto.setMetaDataUsageDto(metaDataUsageDto);
            getMetaDataAuthorityDto.setMetaDataAuthorityDto(metaDataAuthorityDto);

            getMetaDataAuthorityDto.setAuditMetaDataOption((int) UserMetaDataAuditEnum.AUDIT_PASSED.getValue());

            MetadataUsedQuoDto metadataUsedQuoDto = new MetadataUsedQuoDto();
            metadataUsedQuoDto.setMetadataUsageType(2);
            metadataUsedQuoDto.setExpire(false);
            metadataUsedQuoDto.setUsedTimes(1);
            //对应数据授权信息中元数据的使用实况
            getMetaDataAuthorityDto.setMetadataUsedQuoDto(metadataUsedQuoDto);
            getMetaDataAuthorityDto.setApplyAt(1629877270100L);
            getMetaDataAuthorityDto.setAuditAt(1629877270100L);
            getMetaDataAuthorityDto.setAuditSuggestion("审核意见");
            getMetaDataAuthorityDto.setMetadataAuthorityState((int)UserMetaDataAuthorithStateEnum.RELEASED.getValue());
            metaDataAuthorityDtoList.add(getMetaDataAuthorityDto);
        }
        return metaDataAuthorityDtoList;
    }

    @Override
    public NodeIdentityDto getNodeIdentity() {
        NodeIdentityDto nodeIdentityDto = new NodeIdentityDto();
        nodeIdentityDto.setNodeName("mockNodeName");
        nodeIdentityDto.setNodeId("mockNodeId");
        nodeIdentityDto.setIdentityId("mockIdentityId");
        nodeIdentityDto.setStatus(1);
        return nodeIdentityDto;
    }

    @Override
    public List<NodeIdentityDto> getIdentityList() {
        List<NodeIdentityDto> nodeIdentityDtoList = new ArrayList<>();
        NodeIdentityDto nodeIdentityDto;
        for (int i = 0; i < LEN; i++) {
            nodeIdentityDto = new NodeIdentityDto();
            nodeIdentityDto.setNodeName("节点" + i + "名字");
            nodeIdentityDto.setNodeId("节点" + i + "的Id");
            nodeIdentityDto.setIdentityId("节点" + i + "的identityId");
            nodeIdentityDto.setStatus(1);
            nodeIdentityDtoList.add(nodeIdentityDto);
        }
        return nodeIdentityDtoList;
    }

    @Override
    public List<GetMetaDataAuthorityDto> getGlobalMetadataAuthorityList() {
        List<GetMetaDataAuthorityDto> getMetaDataAuthorityDtoList = new ArrayList<>();
        getMetaDataAuthorityDtoList.add(new GetMetaDataAuthorityDto());
        getMetaDataAuthorityDtoList.add(new GetMetaDataAuthorityDto());
        return getMetaDataAuthorityDtoList;
    }
}
