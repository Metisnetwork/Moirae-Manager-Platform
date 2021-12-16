package com.moirae.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.protobuf.Empty;
import com.moirae.rosettaflow.common.enums.ErrorMsg;
import com.moirae.rosettaflow.common.enums.RespCodeEnum;
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
import io.grpc.ManagedChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

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
        } else {
            //添加或者更新组织表中的identityIp及identityPort
            Organization organization = organizationService.getByIdentityId(nodeIdentity.getOwner().getIdentityId());
            if (null == organization) {
                organization = new Organization();
                organization.setNodeName(nodeIdentity.getOwner().getNodeName());
                organization.setNodeId(nodeIdentity.getOwner().getNodeId());
                organization.setIdentityId(nodeIdentity.getOwner().getIdentityId());
                organization.setIdentityIp(identityIp);
                organization.setIdentityPort(identityPort);
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
        return this.getOne(queryWrapper);
    }

    @Override
    public IPage<UserOrgMaintainDto> queryUserOrgMaintainPageList(String orgName, Long current, Long size) {
        UserDto userDto = commonService.getCurrentUser();
        IPage<WorkflowDto> page = new Page<>(current, size);
        return this.baseMapper.queryUserOrgMaintainPageList(userDto.getAddress(), orgName, page);
    }

    @Override
    public void delIpPortBind(Long id) {
        UserDto userDto = commonService.getCurrentUser();
        LambdaUpdateWrapper<UserOrgMaintain> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(UserOrgMaintain::getAddress, userDto.getAddress());
        wrapper.eq(UserOrgMaintain::getId, id);
        this.baseMapper.delete(wrapper);
    }
}
