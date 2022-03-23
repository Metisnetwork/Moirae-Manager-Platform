package com.moirae.rosettaflow.grpc.client;

import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;
import com.moirae.rosettaflow.common.enums.ErrorMsg;
import com.moirae.rosettaflow.common.enums.MetaDataUsageEnum;
import com.moirae.rosettaflow.common.enums.RespCodeEnum;
import com.moirae.rosettaflow.common.enums.UserMetaDataAuthorithStateEnum;
import com.moirae.rosettaflow.common.exception.BusinessException;
import com.moirae.rosettaflow.grpc.constant.GrpcConstant;
import com.moirae.rosettaflow.grpc.identity.dto.NodeIdentityDto;
import com.moirae.rosettaflow.grpc.metadata.req.dto.ApplyMetaDataAuthorityRequestDto;
import com.moirae.rosettaflow.grpc.metadata.req.dto.MetaDataAuthorityDto;
import com.moirae.rosettaflow.grpc.metadata.req.dto.MetaDataUsageRuleDto;
import com.moirae.rosettaflow.grpc.metadata.req.dto.RevokeMetaDataAuthorityRequestDto;
import com.moirae.rosettaflow.grpc.metadata.resp.dto.ApplyMetaDataAuthorityResponseDto;
import com.moirae.rosettaflow.grpc.metadata.resp.dto.GetMetaDataAuthorityDto;
import com.moirae.rosettaflow.grpc.metadata.resp.dto.MetadataUsedQuoDto;
import com.moirae.rosettaflow.grpc.metadata.resp.dto.RevokeMetadataAuthorityResponseDto;
import com.moirae.rosettaflow.grpc.service.*;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 * @date 2021/9/1
 * @description 身份相关接口
 */
@Slf4j
@Component
public class AuthServiceClient {

    @GrpcClient("carrier-grpc-server")
    AuthServiceGrpc.AuthServiceBlockingStub authServiceBlockingStub;

    /**
     * 元数据授权申请
     * @param requestDto 元数据授权信息
     * @return 授权结果
     */
    public ApplyMetaDataAuthorityResponseDto applyMetaDataAuthority(ApplyMetaDataAuthorityRequestDto requestDto) {
        ApplyMetadataAuthorityRequest.Builder applyMetaDataAuthorityRequest = ApplyMetadataAuthorityRequest.newBuilder();

        //1.设置发起任务的用户的信息
        applyMetaDataAuthorityRequest.setUser(requestDto.getUser());

        //2.设置用户类型
        applyMetaDataAuthorityRequest.setUserTypeValue(requestDto.getUserType());

        //3.元数据使用授权信息
        MetadataAuthority.Builder metaDataAuthorityBuilder = MetadataAuthority.newBuilder();

        //3.1元数据所属的组织信息
        Organization owner = Organization.newBuilder()
                .setIdentityId(requestDto.getAuth().getOwner().getIdentityId())
                .setNodeName(requestDto.getAuth().getOwner().getNodeName())
                .setNodeId(requestDto.getAuth().getOwner().getNodeId())
                .build();
        metaDataAuthorityBuilder.setOwner(owner);

        // 3.2元数据Id
        metaDataAuthorityBuilder.setMetadataId(requestDto.getAuth().getMetaDataId());

        //3.3元数据怎么使用
        MetadataUsageRule.Builder metaDataUsageBuilder = MetadataUsageRule.newBuilder();
        metaDataUsageBuilder.setUsageTypeValue(requestDto.getAuth().getMetaDataUsageDto().getUseType());
        if (requestDto.getAuth().getMetaDataUsageDto().getUseType() == MetaDataUsageEnum.PERIOD.getValue()) {
            if (requestDto.getAuth().getMetaDataUsageDto().getStartAt() == null || requestDto.getAuth().getMetaDataUsageDto().getEndAt() == null) {
                log.error("AuthServiceClient->applyMetaDataAuthority() fail reason:{}", ErrorMsg.APPLY_METADATA_USAGE_TYPE_ERROR.getMsg());
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.APPLY_METADATA_USAGE_TYPE_ERROR.getMsg());
            }
            metaDataUsageBuilder.setStartAt(requestDto.getAuth().getMetaDataUsageDto().getStartAt());
            metaDataUsageBuilder.setEndAt(requestDto.getAuth().getMetaDataUsageDto().getEndAt());
        } else if (requestDto.getAuth().getMetaDataUsageDto().getUseType() == MetaDataUsageEnum.TIMES.getValue()) {
            if (requestDto.getAuth().getMetaDataUsageDto().getTimes() == null) {
                log.error("AuthServiceClient->applyMetaDataAuthority() fail reason:{}", ErrorMsg.APPLY_METADATA_USAGE_TYPE_ERROR.getMsg());
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.APPLY_METADATA_USAGE_TYPE_ERROR.getMsg());
            }
            metaDataUsageBuilder.setTimes(requestDto.getAuth().getMetaDataUsageDto().getTimes());
        }

        metaDataAuthorityBuilder.setUsageRule(metaDataUsageBuilder);
        applyMetaDataAuthorityRequest.setAuth(metaDataAuthorityBuilder.build());

        // 4.发起数据授权申请的账户的签名
        applyMetaDataAuthorityRequest.setSign(ByteString.copyFromUtf8(requestDto.getSign()));

        ApplyMetadataAuthorityResponse applyMetaDataAuthorityResponse = authServiceBlockingStub.applyMetadataAuthority(applyMetaDataAuthorityRequest.build());

        if (applyMetaDataAuthorityResponse.getStatus() != GrpcConstant.GRPC_SUCCESS_CODE) {
            log.error("AuthServiceClient->applyMetaDataAuthority() fail reason:{}", applyMetaDataAuthorityResponse.getMsg());
            throw new BusinessException(applyMetaDataAuthorityResponse.getStatus(), applyMetaDataAuthorityResponse.getMsg());
        }

        ApplyMetaDataAuthorityResponseDto applyMetaDataAuthorityResponseDto = new ApplyMetaDataAuthorityResponseDto();
        applyMetaDataAuthorityResponseDto.setStatus(applyMetaDataAuthorityResponse.getStatus());
        applyMetaDataAuthorityResponseDto.setMsg(applyMetaDataAuthorityResponse.getMsg());
        applyMetaDataAuthorityResponseDto.setMetaDataAuthId(applyMetaDataAuthorityResponse.getMetadataAuthId());
        return applyMetaDataAuthorityResponseDto;
    }

    /**
     *  撤销元数据授权申请
     * @param requestDto 撤销元数据信息
     * @return 撤销结果
     */
    public RevokeMetadataAuthorityResponseDto revokeMetadataAuthority(RevokeMetaDataAuthorityRequestDto requestDto) {

        RevokeMetadataAuthorityRequest.Builder request = RevokeMetadataAuthorityRequest.newBuilder();
        request.setUser(requestDto.getUser());
        request.setUserTypeValue(requestDto.getUserType());
        request.setMetadataAuthId(requestDto.getMetadataAuthId());
        request.setSign(ByteString.copyFromUtf8(requestDto.getSign()));

        SimpleResponse response = authServiceBlockingStub.revokeMetadataAuthority(request.build());
        if (response.getStatus() != GrpcConstant.GRPC_SUCCESS_CODE) {
            log.error("AuthServiceClient->revokeMetadataAuthority() fail reason:{}", response.getMsg());
            throw new BusinessException(response.getStatus(), response.getMsg());
        }

        RevokeMetadataAuthorityResponseDto responseDto = new RevokeMetadataAuthorityResponseDto();
        responseDto.setStatus(response.getStatus());
        responseDto.setMsg(response.getMsg());
        return responseDto;
    }


    /**
     * 查询(本组织)的所有元数据的授权申请及审核结果详情列表
     *
     * @return 获取数据授权申请列表
     */
    public List<GetMetaDataAuthorityDto> getMetaDataAuthorityList(@NonNull Long latestSynced) {
        List<GetMetaDataAuthorityDto> getMetaDataAuthorityDtoList = new ArrayList<>();

        GetMetadataAuthorityListRequest request = GetMetadataAuthorityListRequest.newBuilder()
                .setLastUpdated(latestSynced)
                .setPageSize(GrpcConstant.PAGE_SIZE)
                .build();
        GetMetadataAuthorityListResponse getMetaDataAuthorityListResponse = authServiceBlockingStub.getLocalMetadataAuthorityList(request);
        return getGetMetaDataAuthorityDtoList(getMetaDataAuthorityDtoList, getMetaDataAuthorityListResponse);
    }

    /**
     * 查询(全网)的所有元数据的授权申请及审核结果详情列表
     *
     * @return 授权申请及审核结果详情列表
     */
    public List<GetMetaDataAuthorityDto> getGlobalMetadataAuthorityList(@NonNull Long latestSynced) {
        List<GetMetaDataAuthorityDto> getMetaDataAuthorityDtoList = new ArrayList<>();

        GetMetadataAuthorityListRequest request = GetMetadataAuthorityListRequest.newBuilder()
                .setLastUpdated(latestSynced)
                .setPageSize(GrpcConstant.PAGE_SIZE)
                .build();
        GetMetadataAuthorityListResponse getMetadataAuthorityListResponse = authServiceBlockingStub.getGlobalMetadataAuthorityList(request);
        return getGetMetaDataAuthorityDtoList(getMetaDataAuthorityDtoList, getMetadataAuthorityListResponse);
    }

    private List<GetMetaDataAuthorityDto> getGetMetaDataAuthorityDtoList(List<GetMetaDataAuthorityDto> getMetaDataAuthorityDtoList, GetMetadataAuthorityListResponse getMetadataAuthorityListResponse) {
        if (getMetadataAuthorityListResponse.getStatus() != GrpcConstant.GRPC_SUCCESS_CODE) {
            throw new BusinessException(getMetadataAuthorityListResponse.getStatus(), getMetadataAuthorityListResponse.getMsg());
        }

        GetMetaDataAuthorityDto getMetaDataAuthorityDto;
        MetaDataAuthorityDto metaDataAuthorityDto;
        NodeIdentityDto owner;
        MetaDataUsageRuleDto metaDataUsageRuleDto;
        for (int i = 0; i < getMetadataAuthorityListResponse.getListCount(); i++) {
            //过滤掉授权信息状态未知的数据
            if (getMetadataAuthorityListResponse.getList(i).getStateValue() == UserMetaDataAuthorithStateEnum.UNKNOWN.getValue()) {
                continue;
            }

            getMetaDataAuthorityDto = new GetMetaDataAuthorityDto();

            owner = new NodeIdentityDto();
            owner.setNodeName(getMetadataAuthorityListResponse.getList(i).getAuth().getOwner().getNodeName());
            owner.setNodeId(getMetadataAuthorityListResponse.getList(i).getAuth().getOwner().getNodeId());
            owner.setIdentityId(getMetadataAuthorityListResponse.getList(i).getAuth().getOwner().getIdentityId());

            metaDataUsageRuleDto = new MetaDataUsageRuleDto();
            metaDataUsageRuleDto.setUseType(getMetadataAuthorityListResponse.getList(i).getAuth().getUsageRule().getUsageTypeValue());
            metaDataUsageRuleDto.setStartAt(getMetadataAuthorityListResponse.getList(i).getAuth().getUsageRule().getStartAt());
            metaDataUsageRuleDto.setEndAt(getMetadataAuthorityListResponse.getList(i).getAuth().getUsageRule().getEndAt());
            metaDataUsageRuleDto.setTimes(getMetadataAuthorityListResponse.getList(i).getAuth().getUsageRule().getTimes());

            metaDataAuthorityDto = new MetaDataAuthorityDto();
            metaDataAuthorityDto.setOwner(owner);
            metaDataAuthorityDto.setMetaDataId(getMetadataAuthorityListResponse.getList(i).getAuth().getMetadataId());
            metaDataAuthorityDto.setMetaDataUsageDto(metaDataUsageRuleDto);

            MetadataUsedQuoDto metadataUsedQuoDto = new MetadataUsedQuoDto();
            metadataUsedQuoDto.setMetadataUsageType(getMetadataAuthorityListResponse.getList(i).getUsedQuo().getUsageTypeValue());
            metadataUsedQuoDto.setExpire(getMetadataAuthorityListResponse.getList(i).getUsedQuo().getExpire());
            metadataUsedQuoDto.setUsedTimes(getMetadataAuthorityListResponse.getList(i).getUsedQuo().getUsedTimes());

            //元数据授权申请Id
            getMetaDataAuthorityDto.setMetaDataAuthId(getMetadataAuthorityListResponse.getList(i).getMetadataAuthId());
            //发起任务的用户的信息 (task是属于用户的)
            getMetaDataAuthorityDto.setUser(getMetadataAuthorityListResponse.getList(i).getUser());
            //用户类型 (0: 未定义; 1: 第二地址; 2: 测试网地址; 3: 主网地址)
            getMetaDataAuthorityDto.setUserType((getMetadataAuthorityListResponse.getList(i).getUserTypeValue()));
            //元数据使用授权信息
            getMetaDataAuthorityDto.setMetaDataAuthorityDto(metaDataAuthorityDto);
            //审核结果:0-等待审核中 1-审核通过 2-审核拒绝
            getMetaDataAuthorityDto.setAuditMetaDataOption(getMetadataAuthorityListResponse.getList(i).getAuditOptionValue());
            //审核意见 (允许""字符)
            getMetaDataAuthorityDto.setAuditSuggestion(getMetadataAuthorityListResponse.getList(i).getAuditSuggestion());
            //对应数据授权信息中元数据的使用实况
            getMetaDataAuthorityDto.setMetadataUsedQuoDto(metadataUsedQuoDto);
            //发起授权申请的时间 (单位: ms)
            getMetaDataAuthorityDto.setApplyAt(getMetadataAuthorityListResponse.getList(i).getApplyAt());
            //审核授权申请的时间 (单位: ms)
            getMetaDataAuthorityDto.setAuditAt(getMetadataAuthorityListResponse.getList(i).getAuditAt());
            //数据授权信息的状态 (0: 未知; 1: 还未发布的数据授权; 2: 已发布的数据授权; 3: 已撤销的数据授权 <失效前主动撤回的>; 4: 已经失效的数据授权 <过期or达到使用上限的>)
            getMetaDataAuthorityDto.setMetadataAuthorityState(getMetadataAuthorityListResponse.getList(i).getStateValue());
            //数据的最后更新时间，UTC毫秒数
            getMetaDataAuthorityDto.setUpdateAt(getMetadataAuthorityListResponse.getList(i).getUpdateAt());

            getMetaDataAuthorityDtoList.add(getMetaDataAuthorityDto);
        }
        return getMetaDataAuthorityDtoList;
    }

    /**
     * 查询自己组织的identity信息
     *
     * @return 返回组织信息
     */
    public NodeIdentityDto getNodeIdentity() {
        Empty empty = Empty.newBuilder().build();
        GetNodeIdentityResponse nodeIdentity = authServiceBlockingStub.getNodeIdentity(empty);

        if (nodeIdentity.getStatus() != GrpcConstant.GRPC_SUCCESS_CODE) {
            log.error("AuthServiceClient->getNodeIdentity() fail reason:{}", nodeIdentity.getMsg());
            throw new BusinessException(nodeIdentity.getStatus(), nodeIdentity.getMsg());
        }

        NodeIdentityDto nodeIdentityDto = new NodeIdentityDto();
        nodeIdentityDto.setNodeName(nodeIdentity.getOwner().getNodeName());
        nodeIdentityDto.setNodeId(nodeIdentity.getOwner().getNodeId());
        nodeIdentityDto.setIdentityId(nodeIdentity.getOwner().getIdentityId());
        nodeIdentityDto.setStatus(nodeIdentity.getOwner().getStatusValue());
        return nodeIdentityDto;
    }

    public List<NodeIdentityDto> getIdentityList(@NonNull Long latestSynced) {
        List<NodeIdentityDto> memberList = new ArrayList<>();

        GetIdentityListRequest request = GetIdentityListRequest.newBuilder()
                .setLastUpdated(latestSynced)
                .setPageSize(GrpcConstant.PAGE_SIZE)
                .build();
        GetIdentityListResponse getIdentityListResponse = authServiceBlockingStub.getIdentityList(request);

        if (getIdentityListResponse.getStatus() != GrpcConstant.GRPC_SUCCESS_CODE) {
            log.error("AuthServiceClient->getIdentityList() fail reason:{}", getIdentityListResponse.getMsg());
            throw new BusinessException(getIdentityListResponse.getStatus(), getIdentityListResponse.getMsg());
        }

        NodeIdentityDto member;
        for (int i = 0; i < getIdentityListResponse.getMemberListCount(); i++) {
            member = new NodeIdentityDto();
            member.setNodeName(getIdentityListResponse.getMemberList(i).getNodeName());
            member.setNodeId(getIdentityListResponse.getMemberList(i).getNodeId());
            member.setIdentityId(getIdentityListResponse.getMemberList(i).getIdentityId());
            member.setStatus(getIdentityListResponse.getMemberList(i).getStatusValue());
            member.setUpdateAt(getIdentityListResponse.getMemberList(i).getUpdateAt());
            member.setImageUrl(getIdentityListResponse.getMemberList(i).getImageUrl());
            member.setDetails(getIdentityListResponse.getMemberList(i).getDetails());
            memberList.add(member);
        }
        return memberList;
    }
}
