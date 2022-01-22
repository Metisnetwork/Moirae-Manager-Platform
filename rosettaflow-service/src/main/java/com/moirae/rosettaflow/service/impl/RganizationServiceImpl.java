package com.moirae.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.protobuf.Empty;
import com.moirae.rosettaflow.common.enums.ErrorMsg;
import com.moirae.rosettaflow.common.enums.RespCodeEnum;
import com.moirae.rosettaflow.common.enums.StatusEnum;
import com.moirae.rosettaflow.common.exception.BusinessException;
import com.moirae.rosettaflow.dto.UserDto;
import com.moirae.rosettaflow.grpc.constant.GrpcConstant;
import com.moirae.rosettaflow.grpc.service.AuthServiceGrpc;
import com.moirae.rosettaflow.grpc.service.GetNodeIdentityResponse;
import com.moirae.rosettaflow.grpc.service.GrpcAuthService;
import com.moirae.rosettaflow.mapper.OrganizationMapper;
import com.moirae.rosettaflow.mapper.UserOrgMapper;
import com.moirae.rosettaflow.mapper.domain.Organization;
import com.moirae.rosettaflow.mapper.domain.UserOrg;
import com.moirae.rosettaflow.service.CommonService;
import com.moirae.rosettaflow.service.IOrganizationService;
import com.moirae.rosettaflow.service.NetManager;
import io.grpc.ManagedChannel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @author hudenian
 * @date 2021/9/26
 * @description 机构服务实现类
 */
@Slf4j
@Service
public class RganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization> implements IOrganizationService {

    @Resource
    private CommonService commonService;

    @Resource
    private NetManager netManager;

    @Resource
    private UserOrgMapper userOrgMapper;

    @Resource
    private GrpcAuthService grpcAuthService;

    @Override
    public void batchInsert(List<Organization> organizationList) {
        this.baseMapper.batchInsert(organizationList);
    }

    @Override
    public Organization getByIdentityId(String identityId) {
        LambdaQueryWrapper<Organization> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Organization::getIdentityId, identityId);
        wrapper.eq(Organization::getStatus, StatusEnum.VALID.getValue());
        return this.getOne(wrapper);
    }

    @Override
    public List<Organization> getByIdentityIds(Object[] identityArr) {
        LambdaQueryWrapper<Organization> wrapper = Wrappers.lambdaQuery();
        wrapper.in(Organization::getIdentityId, identityArr);
        wrapper.eq(Organization::getStatus, StatusEnum.VALID.getValue());
        return this.list(wrapper);
    }

    @Override
    public List<Organization> getAllIdentity() {
        LambdaQueryWrapper<Organization> wrapper = Wrappers.lambdaQuery();
        wrapper.in(Organization::getStatus, StatusEnum.VALID.getValue());
        return this.list(wrapper);
    }

    @Override
    public List<Organization> getAllByUser(String hexAddress) {
        return this.baseMapper.selectByUser(hexAddress);
    }

    @Override
    public List<Organization> getAllByUserSession() {
        UserDto userDto = commonService.getCurrentUser();
        List<Organization> organizationList = getAllByUser(userDto.getAddress());
        organizationList.forEach(organization -> {
            if(StringUtils.isNoneBlank(userDto.getOrgIdentityId()) && userDto.getOrgIdentityId().equals(organization.getIdentityId())){
                organization.setDefaultConnectFlag((byte)1);
            }
        });
        return organizationList;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void addUserOrganization(String identityIp, Integer identityPort) {
        // 查询组织信息
        ManagedChannel managedChannel = netManager.assemblyChannel(identityIp, identityPort);
        Empty empty = Empty.newBuilder().build();
        GetNodeIdentityResponse nodeIdentity = null;
        try {
            nodeIdentity = AuthServiceGrpc.newBlockingStub(managedChannel).getNodeIdentity(empty);
        } catch (Exception e){
            log.error("AuthServiceClient->addUserOrganization() getNodeIdentity error",e);
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.ORGANIZATION_INFO_ERROR.getMsg());
        }

        if (nodeIdentity.getStatus() != GrpcConstant.GRPC_SUCCESS_CODE) {
            log.error("AuthServiceClient->getNodeIdentity() fail reason:{}", nodeIdentity.getMsg());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.ORGANIZATION_INFO_ERROR.getMsg());
        }
        // 添加或更新组织信息
        Organization organization = getByIdentityId(nodeIdentity.getOwner().getIdentityId());
        if (null == organization) {
            organization = new Organization();
            organization.setNodeName(nodeIdentity.getOwner().getNodeName());
            organization.setNodeId(nodeIdentity.getOwner().getNodeId());
            organization.setIdentityId(nodeIdentity.getOwner().getIdentityId());
            organization.setIdentityIp(identityIp);
            organization.setIdentityPort(identityPort);
            // 新添加的不是公共组织
            organization.setPublicFlag((byte)0);
            save(organization);
        } else {
            if (!organization.getIdentityIp().equals(identityIp) || organization.getIdentityPort() != identityPort.intValue()) {
                organization.setIdentityIp(identityIp);
                organization.setIdentityPort(identityPort);
                updateById(organization);
            }
        }
        // 绑定用户私有组织关系
        UserDto userDto = commonService.getCurrentUser();
        //获取原先的用户组织绑定关系信息，存在则更新，不存在则添加
        LambdaQueryWrapper<UserOrg> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserOrg::getOrgIdentityId, organization.getIdentityId());
        wrapper.eq(UserOrg::getUserAddress, userDto.getAddress());
        UserOrg userOrg = userOrgMapper.selectOne(wrapper);
        if (null == userOrg) {
            userOrg = new UserOrg();
            userOrg.setUserAddress(userDto.getAddress());
            userOrg.setOrgIdentityId(organization.getIdentityId());
            userOrgMapper.insert(userOrg);
        }
        //netManager中添加此channel
        netManager.addChannel(organization.getIdentityId(), managedChannel, identityIp, identityPort);
    }

    @Override
    public void deleteUserOrganization(String identityId) {
        // 删除用户组织关系
        UserDto userDto = commonService.getCurrentUser();
        LambdaUpdateWrapper<UserOrg> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(UserOrg::getUserAddress, userDto.getAddress());
        wrapper.eq(UserOrg::getOrgIdentityId, identityId);
        userOrgMapper.delete(wrapper);
    }

    @Override
    public void isValid(Set<String> orgId) {
        List<Organization> organizationList = getByIdentityIds(orgId.toArray());
        if(orgId.size() != organizationList.size()){
            log.error("AssemblyNodeInput->前端输入的机构信息identity:{}未找到", orgId);
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.ORGANIZATION_NOT_EXIST.getMsg());
        }
    }
}
