package com.moirae.rosettaflow.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.protobuf.Empty;
import com.moirae.rosettaflow.common.constants.SysConstant;
import com.moirae.rosettaflow.common.enums.ErrorMsg;
import com.moirae.rosettaflow.common.enums.RespCodeEnum;
import com.moirae.rosettaflow.common.enums.StatusEnum;
import com.moirae.rosettaflow.common.enums.ValidFlagEnum;
import com.moirae.rosettaflow.common.exception.BusinessException;
import com.moirae.rosettaflow.dto.UserDto;
import com.moirae.rosettaflow.dto.UserOrgMaintainDto;
import com.moirae.rosettaflow.dto.WorkflowDto;
import com.moirae.rosettaflow.grpc.constant.GrpcConstant;
import com.moirae.rosettaflow.grpc.service.AuthServiceGrpc;
import com.moirae.rosettaflow.grpc.service.GetNodeIdentityResponse;
import com.moirae.rosettaflow.mapper.UserOrgMaintainMapper;
import com.moirae.rosettaflow.mapper.domain.Organization;
import com.moirae.rosettaflow.mapper.domain.UserOrgMaintain;
import com.moirae.rosettaflow.service.CommonService;
import com.moirae.rosettaflow.service.IOrganizationService;
import com.moirae.rosettaflow.service.IUserOrgMaintainService;
import com.moirae.rosettaflow.service.NetManager;
import com.zengtengpeng.operation.RedissonObject;
import io.grpc.ManagedChannel;
import jnr.ffi.annotations.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 用户组织连接绑定关系服务类
 *
 * @author hudenian
 * @date 2021/12/15
 */
@Slf4j
@Service
public class UserOrgMaintainServiceImpl extends ServiceImpl<UserOrgMaintainMapper, UserOrgMaintain> implements IUserOrgMaintainService {

    @Resource
    private CommonService commonService;

    @Resource
    private IOrganizationService organizationService;

    @Resource
    private IUserOrgMaintainService userOrgMaintainService;

    @Resource
    private NetManager netManager;

    @Resource
    private RedissonObject redissonObject;



    @Override
    public List<UserOrgMaintainDto> queryUserOrgMaintainPageList() {
        UserDto userDto = commonService.getCurrentUser();
        List<UserOrgMaintainDto> userOrgMaintainDtoList = this.baseMapper.queryUserOrgMaintainPageList(userDto.getAddress());
        if (CollectionUtil.isEmpty(userOrgMaintainDtoList)) {
            return userOrgMaintainDtoList;
        }
        userOrgMaintainDtoList.forEach(userOrgMaintainDto -> {
            // 默认未连接
            userOrgMaintainDto.setConnectFlag((byte)0);
            if (userOrgMaintainDto.getIdentityId().equals(userDto.getIdentityId())) {
                // 取出缓存中，已连接的组织，设置为已连接
                userOrgMaintainDto.setConnectFlag((byte)1);
            }
        });
        return userOrgMaintainDtoList;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void ipPortBind(String identityIp, Integer identityPort) {
        UserDto userDto = commonService.getCurrentUser();
        ManagedChannel managedChannel = netManager.assemblyChannel(identityIp, identityPort);
        Empty empty = Empty.newBuilder().build();
        GetNodeIdentityResponse nodeIdentity = AuthServiceGrpc.newBlockingStub(managedChannel).getNodeIdentity(empty);
        if (nodeIdentity.getStatus() != GrpcConstant.GRPC_SUCCESS_CODE) {
            log.error("AuthServiceClient->getNodeIdentity() fail reason:{}", nodeIdentity.getMsg());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.ORGANIZATION_INFO_ERROR.getMsg());
        }
        //添加或者更新组织表中的identityIp及identityPort
        Organization organization = organizationService.getByIdentityId(nodeIdentity.getOwner().getIdentityId());
        if (null == organization) {
            organization = new Organization();
            organization.setNodeName(nodeIdentity.getOwner().getNodeName());
            organization.setNodeId(nodeIdentity.getOwner().getNodeId());
            organization.setIdentityId(nodeIdentity.getOwner().getIdentityId());
            organization.setIdentityIp(identityIp);
            organization.setIdentityPort(identityPort);
            // 新添加的不是公共组织
            organization.setPublicFlag((byte)0);
            organizationService.save(organization);
        } else {
            if (!organization.getIdentityIp().equals(identityIp) || organization.getIdentityPort() != identityPort.intValue()) {
                organization.setIdentityIp(identityIp);
                organization.setIdentityPort(identityPort);
                organization.setUpdateTime(new Date());
                organizationService.updateById(organization);

                //把其它用户绑定此identity的连接有效状态置成无效
                this.updateValidFlag(organization.getIdentityId(), identityIp, identityPort);
            }
        }

        //获取原先的用户组织绑定关系信息，存在则更新，不存在则添加
        UserOrgMaintain userOrgMaintain = this.getByAddressAndIdentityId(userDto.getAddress(), organization.getIdentityId());
        if (null == userOrgMaintain) {
            userOrgMaintain = new UserOrgMaintain();
            userOrgMaintain.setAddress(userDto.getAddress());
            userOrgMaintain.setIdentityId(organization.getIdentityId());
            userOrgMaintain.setIdentityIp(organization.getIdentityIp());
            userOrgMaintain.setIdentityPort(organization.getIdentityPort());
            userOrgMaintainService.save(userOrgMaintain);
        } else {
            userOrgMaintain.setIdentityIp(organization.getIdentityIp());
            userOrgMaintain.setIdentityPort(organization.getIdentityPort());
            userOrgMaintain.setUpdateTime(new Date());
            userOrgMaintainService.updateById(userOrgMaintain);
        }

        //netManager中添加此channel
        netManager.addChannel(organization.getIdentityId(), managedChannel, identityIp, identityPort);
    }

    /**
     * 更新用户组织连接绑定关系表中ip及端口绑定无效的记录
     *
     * @param identityId   组织identityId
     * @param identityIp   组织ip
     * @param identityPort 组织端口
     */
    @Override
    public void updateValidFlag(String identityId, String identityIp, Integer identityPort) {
        LambdaUpdateWrapper<UserOrgMaintain> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(UserOrgMaintain::getValidFlag, ValidFlagEnum.UN_VALID.getValue());
        updateWrapper.eq(UserOrgMaintain::getIdentityId, identityId);
        updateWrapper.and(i -> i.ne(UserOrgMaintain::getIdentityIp, identityIp).or(p -> p.ne(UserOrgMaintain::getIdentityPort, identityPort)));
        this.update(updateWrapper);
    }

    @Override
    public UserOrgMaintain getByAddressAndIdentityId(String address, String identityId) {
        LambdaQueryWrapper<UserOrgMaintain> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(UserOrgMaintain::getAddress, address);
        queryWrapper.eq(UserOrgMaintain::getIdentityId, identityId);
        queryWrapper.eq(UserOrgMaintain::getValidFlag, StatusEnum.VALID.getValue());
        queryWrapper.eq(UserOrgMaintain::getStatus, StatusEnum.VALID.getValue());
        return this.getOne(queryWrapper);
    }

    @Override
    public void connectIdentity(String identityId) {
        UserDto userDto = commonService.getCurrentUser();
        // 判断是否连接公组织
        Organization organization = organizationService.getByIdentityIAndPublicFlag(identityId, StatusEnum.VALID.getValue());
        if (Objects.nonNull(organization)) {
            // 验证此组织是否可连接
            this. checkConnectIdentity(userDto, identityId, organization.getIdentityIp(), organization.getIdentityPort());
            return;
        }
        // 判断是否连接用户组织
        UserOrgMaintain userOrgMaintain = this.getByAddressAndIdentityId(userDto.getAddress(), identityId);
        if (Objects.isNull(userOrgMaintain)) {
            log.error("AuthServiceClient-connectIdentity失败, identityId:{}, userDto:{}", identityId, userDto);
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.USER_IDENTITY_ERROR.getMsg());
        }
        // 验证此组织是否可连接
        this. checkConnectIdentity(userDto, identityId, userOrgMaintain.getIdentityIp(), userOrgMaintain.getIdentityPort());
    }

    @Override
    public void checkConnectIdentity(UserDto userDto, String identityId, String identityIp, Integer identityPort){
        ManagedChannel managedChannel = netManager.assemblyChannel(identityIp, identityPort);
        GetNodeIdentityResponse nodeResp = AuthServiceGrpc.newBlockingStub(managedChannel).getNodeIdentity(Empty.newBuilder().build());
        if (nodeResp.getStatus() != GrpcConstant.GRPC_SUCCESS_CODE) {
            log.error("AuthServiceClient-connectIdentity失败, nodeResp:{}", nodeResp);
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.ORGANIZATION_INFO_ERROR.getMsg());
        }
        userDto.setIdentityId(identityId);
        // 更新用户缓存，存入连接的组织
        redissonObject.setValue(SysConstant.REDIS_USER_PREFIX_KEY + userDto.getToken(), userDto);
    }

    @Override
    public void disconnectIdentity(String identityId) {
        UserDto userDto = commonService.getCurrentUser();
        userDto.setIdentityId(null);
        // 更新用户缓存，存入连接的组织
        redissonObject.setValue(SysConstant.REDIS_USER_PREFIX_KEY + userDto.getToken(), userDto);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delIpPortBind(String identityId) {
        UserDto userDto = commonService.getCurrentUser();
        // 先删除用户组织
        LambdaUpdateWrapper<UserOrgMaintain> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(UserOrgMaintain::getAddress, userDto.getAddress());
        wrapper.eq(UserOrgMaintain::getIdentityId, identityId);
        this.baseMapper.delete(wrapper);
        // 删除组织表组织
        organizationService.deleteByIdentityId(identityId);
    }

}
