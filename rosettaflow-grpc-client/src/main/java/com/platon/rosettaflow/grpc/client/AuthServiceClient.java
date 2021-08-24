package com.platon.rosettaflow.grpc.client;

import com.google.protobuf.ByteString;
import com.platon.rosettaflow.common.enums.ErrorMsg;
import com.platon.rosettaflow.common.enums.MetaDataUsageEnum;
import com.platon.rosettaflow.common.enums.RespCodeEnum;
import com.platon.rosettaflow.common.exception.BusinessException;
import com.platon.rosettaflow.grpc.constant.GrpcConstant;
import com.platon.rosettaflow.grpc.identity.dto.NodeIdentityDto;
import com.platon.rosettaflow.grpc.metadata.req.dto.ApplyMetaDataAuthorityRequestDto;
import com.platon.rosettaflow.grpc.metadata.req.dto.MetaDataAuthorityDto;
import com.platon.rosettaflow.grpc.metadata.req.dto.MetaDataUsageDto;
import com.platon.rosettaflow.grpc.metadata.resp.dto.ApplyMetaDataAuthorityResponseDto;
import com.platon.rosettaflow.grpc.metadata.resp.dto.GetMetaDataAuthorityDto;
import com.platon.rosettaflow.grpc.service.*;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 * @date 2021/8/23
 * @description 身份相关接口
 */
@Slf4j
@Component
public class AuthServiceClient {

    @GrpcClient("carrier-grpc-server")
    private AuthServiceGrpc.AuthServiceBlockingStub authServiceBlockingStub;

    public ApplyMetaDataAuthorityResponseDto applyMetaDataAuthority(ApplyMetaDataAuthorityRequestDto requestDto) {
        ApplyMetaDataAuthorityRequest.Builder applyMetaDataAuthorityRequest = ApplyMetaDataAuthorityRequest.newBuilder();

        //设置发起任务的用户的信息
        applyMetaDataAuthorityRequest.setUser(requestDto.getUser());

        //设置用户类型
        applyMetaDataAuthorityRequest.setUserType(CommonMessage.UserType.forNumber(requestDto.getUserType()));

        MetaDataAuthority.Builder metaDataAuthorityBuilder = MetaDataAuthority.newBuilder();

        //元数据所属用户
        CommonMessage.OrganizationIdentityInfo owner = CommonMessage.OrganizationIdentityInfo.newBuilder()
                .setIdentityId(requestDto.getAuth().getOwner().getIdentityId())
                .setName(requestDto.getAuth().getOwner().getName())
                .setNodeId(requestDto.getAuth().getOwner().getNodeId())
                .build();

        //元数据怎么使用请求对象
        MetaDataUsage.Builder metaDataUsageBuilder = MetaDataUsage.newBuilder();
        metaDataUsageBuilder.setUsageType(MetaDataUsageType.forNumber(requestDto.getAuth().getMetaDataUsageDto().getUserType()));
        if (requestDto.getUserType() == MetaDataUsageEnum.PERIOD.getValue()) {
            if (requestDto.getAuth().getMetaDataUsageDto().getStartAt() == null && requestDto.getAuth().getMetaDataUsageDto().getEndAt() == null) {
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.APPLY_METADATA_USAGE_TYPE_ERROR.getMsg());
            }
        } else if (requestDto.getUserType() == MetaDataUsageEnum.TIMES.getValue()) {
            if (requestDto.getAuth().getMetaDataUsageDto().getTimes() == null) {
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.APPLY_METADATA_USAGE_TYPE_ERROR.getMsg());
            }
        }

        metaDataUsageBuilder.setStartAt(requestDto.getAuth().getMetaDataUsageDto().getStartAt());
        metaDataUsageBuilder.setEndAt(requestDto.getAuth().getMetaDataUsageDto().getEndAt());
        metaDataUsageBuilder.setTimes(requestDto.getAuth().getMetaDataUsageDto().getTimes().intValue());

        //设置元数据使用授权信息
        applyMetaDataAuthorityRequest.setAuth(metaDataAuthorityBuilder.build());

        metaDataAuthorityBuilder.setOwner(owner);
        metaDataAuthorityBuilder.setMetaDataId(requestDto.getAuth().getMetaDataId());
        metaDataAuthorityBuilder.setUsage(metaDataUsageBuilder.build());

        //设置签名
        applyMetaDataAuthorityRequest.setSign(ByteString.copyFromUtf8(requestDto.getSign()));

        ApplyMetaDataAuthorityResponse applyMetaDataAuthorityResponse = authServiceBlockingStub.applyMetaDataAuthority(applyMetaDataAuthorityRequest.build());

        if (applyMetaDataAuthorityResponse.getStatus() != GrpcConstant.GRPC_SUCCESS_CODE) {
            throw new BusinessException(applyMetaDataAuthorityResponse.getStatus(), applyMetaDataAuthorityResponse.getMsg());
        }

        //TODO 国际化处理
        ApplyMetaDataAuthorityResponseDto applyMetaDataAuthorityResponseDto = new ApplyMetaDataAuthorityResponseDto();
        applyMetaDataAuthorityResponseDto.setStatus(applyMetaDataAuthorityResponse.getStatus());
        applyMetaDataAuthorityResponseDto.setMsg(applyMetaDataAuthorityResponse.getMsg());
        applyMetaDataAuthorityResponseDto.setMetaDataAuthId(applyMetaDataAuthorityResponse.getMetaDataAuthId());
        return applyMetaDataAuthorityResponseDto;
    }

    /**
     * @return 获取元数据审核表列
     */
    public List<GetMetaDataAuthorityDto> getMetaDataAuthorityList() {
        List<GetMetaDataAuthorityDto> getMetaDataAuthorityDtoList = new ArrayList<>();

        CommonMessage.EmptyGetParams emptyGetParams = CommonMessage.EmptyGetParams.newBuilder().build();
        GetMetaDataAuthorityListResponse getMetaDataAuthorityListResponse = authServiceBlockingStub.getMetaDataAuthorityList(emptyGetParams);

        if (getMetaDataAuthorityListResponse.getStatus() != GrpcConstant.GRPC_SUCCESS_CODE) {
            throw new BusinessException(getMetaDataAuthorityListResponse.getStatus(), getMetaDataAuthorityListResponse.getMsg());
        }

        GetMetaDataAuthorityDto getMetaDataAuthorityDto;
        MetaDataAuthorityDto metaDataAuthorityDto;
        NodeIdentityDto owner;
        MetaDataUsageDto metaDataUsageDto;
        for (int i = 0; i < getMetaDataAuthorityListResponse.getListCount(); i++) {
            getMetaDataAuthorityDto = new GetMetaDataAuthorityDto();
            getMetaDataAuthorityDto.setMetaDataAuthId(getMetaDataAuthorityListResponse.getList(i).getMetaDataAuthId());
            getMetaDataAuthorityDto.setUser(getMetaDataAuthorityListResponse.getList(i).getUser());
            getMetaDataAuthorityDto.setUserType(getMetaDataAuthorityListResponse.getList(i).getUserTypeValue());

            metaDataAuthorityDto = new MetaDataAuthorityDto();

            owner = new NodeIdentityDto();
            owner.setName(getMetaDataAuthorityListResponse.getList(i).getAuth().getOwner().getName());
            owner.setNodeId(getMetaDataAuthorityListResponse.getList(i).getAuth().getOwner().getNodeId());
            owner.setIdentityId(getMetaDataAuthorityListResponse.getList(i).getAuth().getOwner().getIdentityId());

            metaDataUsageDto = new MetaDataUsageDto();
            metaDataUsageDto.setUserType(getMetaDataAuthorityListResponse.getList(i).getAuth().getUsage().getUsageTypeValue());
            metaDataUsageDto.setStartAt(getMetaDataAuthorityListResponse.getList(i).getAuth().getUsage().getStartAt());
            metaDataUsageDto.setEndAt(getMetaDataAuthorityListResponse.getList(i).getAuth().getUsage().getEndAt());
            metaDataUsageDto.setTimes((long) getMetaDataAuthorityListResponse.getList(i).getAuth().getUsage().getTimes());

            metaDataAuthorityDto.setOwner(owner);
            metaDataAuthorityDto.setMetaDataId(getMetaDataAuthorityListResponse.getList(i).getAuth().getMetaDataId());
            metaDataAuthorityDto.setMetaDataUsageDto(metaDataUsageDto);

            getMetaDataAuthorityDto.setMetaDataAuthorityDto(metaDataAuthorityDto);

            //审核结果
            getMetaDataAuthorityDto.setAuditMetaDataOption(getMetaDataAuthorityListResponse.getList(i).getAudit().getNumber());

            getMetaDataAuthorityDto.setApplyAt(getMetaDataAuthorityListResponse.getList(i).getApplyAt());
            getMetaDataAuthorityDto.setAuditAt(getMetaDataAuthorityListResponse.getList(i).getAuditAt());

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
        CommonMessage.EmptyGetParams emptyGetParams = CommonMessage.EmptyGetParams.newBuilder().build();
        GetNodeIdentityResponse nodeIdentity = authServiceBlockingStub.getNodeIdentity(emptyGetParams);

        if (nodeIdentity.getStatus() != GrpcConstant.GRPC_SUCCESS_CODE) {
            throw new BusinessException(nodeIdentity.getStatus(), nodeIdentity.getMsg());
        }

        NodeIdentityDto nodeIdentityDto = new NodeIdentityDto();
        nodeIdentityDto.setName(nodeIdentity.getOwner().getName());
        nodeIdentityDto.setNodeId(nodeIdentity.getOwner().getNodeId());
        nodeIdentityDto.setIdentityId(nodeIdentity.getOwner().getIdentityId());

        return nodeIdentityDto;
    }
}
