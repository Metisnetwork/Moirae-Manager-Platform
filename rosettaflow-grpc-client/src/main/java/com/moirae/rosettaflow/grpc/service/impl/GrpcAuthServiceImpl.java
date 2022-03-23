package com.moirae.rosettaflow.grpc.service.impl;

import com.moirae.rosettaflow.grpc.client.AuthServiceClient;
import com.moirae.rosettaflow.grpc.constant.GrpcConstant;
import com.moirae.rosettaflow.grpc.identity.dto.NodeIdentityDto;
import com.moirae.rosettaflow.grpc.metadata.req.dto.ApplyMetaDataAuthorityRequestDto;
import com.moirae.rosettaflow.grpc.metadata.req.dto.RevokeMetaDataAuthorityRequestDto;
import com.moirae.rosettaflow.grpc.metadata.resp.dto.ApplyMetaDataAuthorityResponseDto;
import com.moirae.rosettaflow.grpc.metadata.resp.dto.GetMetaDataAuthorityDto;
import com.moirae.rosettaflow.grpc.metadata.resp.dto.RevokeMetadataAuthorityResponseDto;
import com.moirae.rosettaflow.grpc.service.GrpcAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/24
 * @description 功能描述
 */
@Slf4j
@Service
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
    public List<GetMetaDataAuthorityDto> getMetaDataAuthorityList(Long latestSynced) {
        return authServiceClient.getMetaDataAuthorityList(latestSynced);
    }

    @Override
    public List<GetMetaDataAuthorityDto> getAllMetaDataAuthorityList() {
        long latestSynced = 0;
        List<GetMetaDataAuthorityDto> allList = new ArrayList<>();
        List<GetMetaDataAuthorityDto> list;
        do {
            list = authServiceClient.getMetaDataAuthorityList(latestSynced);
            if(list.isEmpty()){
                break;
            }
            allList.addAll(list);
            latestSynced = list.get(list.size() - 1).getUpdateAt();
        } while (list.size() == GrpcConstant.PAGE_SIZE);//如果小于pageSize说明是最后一批了
        return allList;
    }

    @Override
    public NodeIdentityDto getNodeIdentity() {
        return authServiceClient.getNodeIdentity();
    }

    @Override
    public List<NodeIdentityDto> getIdentityList(Long latestSynced) {
        return authServiceClient.getIdentityList(latestSynced);
    }

    @Override
    public List<NodeIdentityDto> getAllIdentityList() {
        long latestSynced = 0;
        List<NodeIdentityDto> allList = new ArrayList<>();
        List<NodeIdentityDto> list;
        do {
            list = authServiceClient.getIdentityList(latestSynced);
            if(list.isEmpty()){
                break;
            }
            allList.addAll(list);
            latestSynced = list.get(list.size() - 1).getUpdateAt();
        } while (list.size() == GrpcConstant.PAGE_SIZE);//如果小于pageSize说明是最后一批了
        return allList;
    }

    @Override
    public List<GetMetaDataAuthorityDto> getGlobalMetadataAuthorityList(Long latestSynced) {
        return authServiceClient.getGlobalMetadataAuthorityList(latestSynced);
    }

    @Override
    public List<GetMetaDataAuthorityDto> getAllGlobalMetadataAuthorityList() {
        long latestSynced = 0;
        List<GetMetaDataAuthorityDto> allList = new ArrayList<>();
        List<GetMetaDataAuthorityDto> list;
        do {
            list = authServiceClient.getGlobalMetadataAuthorityList(latestSynced);
            if(list.isEmpty()){
                break;
            }
            allList.addAll(list);
            latestSynced = list.get(list.size() - 1).getUpdateAt();
        } while (list.size() == GrpcConstant.PAGE_SIZE);//如果小于pageSize说明是最后一批了
        return allList;
    }

}
