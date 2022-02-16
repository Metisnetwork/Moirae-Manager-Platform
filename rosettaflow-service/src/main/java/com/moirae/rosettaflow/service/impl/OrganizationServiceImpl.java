package com.moirae.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.protobuf.Empty;
import com.moirae.rosettaflow.common.constants.SysConfig;
import com.moirae.rosettaflow.common.enums.ErrorMsg;
import com.moirae.rosettaflow.common.enums.RespCodeEnum;
import com.moirae.rosettaflow.common.exception.BusinessException;
import com.moirae.rosettaflow.dto.UserDto;
import com.moirae.rosettaflow.grpc.constant.GrpcConstant;
import com.moirae.rosettaflow.grpc.service.AuthServiceGrpc;
import com.moirae.rosettaflow.grpc.service.GetNodeIdentityResponse;
import com.moirae.rosettaflow.manager.OrgExpandManager;
import com.moirae.rosettaflow.manager.OrgManager;
import com.moirae.rosettaflow.manager.UserOrgManager;
import com.moirae.rosettaflow.mapper.domain.Org;
import com.moirae.rosettaflow.mapper.domain.OrgExpand;
import com.moirae.rosettaflow.mapper.domain.Organization;
import com.moirae.rosettaflow.mapper.domain.UserOrg;
import com.moirae.rosettaflow.mapper.enums.OrgStatusEnum;
import com.moirae.rosettaflow.service.CommonService;
import com.moirae.rosettaflow.service.OrganizationService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Resource
    private SysConfig sysConfig;
    @Resource
    private OrgExpandManager orgExpandManager;
    @Resource
    private OrgManager orgManager;
    @Resource
    private UserOrgManager userOrgManager;
    @Resource
    private CommonService commonService;

    private final Map<String, ManagedChannel> channelMap = new ConcurrentHashMap<>();

    @Override
    public void initPublicOrg() {
        if(orgExpandManager.count() == 0 && sysConfig.getPublicOrgList().size() > 0){
            log.info("init public organization! config = {}", sysConfig.getPublicOrgList());
            List<OrgExpand> orgExpandList = sysConfig.getPublicOrgList().stream()
                    .map(item -> {
                        OrgExpand orgExpand = new OrgExpand();
                        orgExpand.setIdentityId(item.getIdentity());
                        orgExpand.setIdentityIp(item.getHost());
                        orgExpand.setIdentityPort(item.getPort());
                        orgExpand.setIsPublic(true);
                        return orgExpand;
                    })
                    .collect(Collectors.toList());
            if(orgExpandList.size() > 0){
                orgExpandManager.saveBatch(orgExpandList);
            }
        }
    }

    @Override
    public ManagedChannel getChannel(String identityId) {
        return channelMap.computeIfAbsent(identityId, key ->{
            OrgExpand orgExpand = orgExpandManager.getById(identityId);

            if (null == orgExpand) {
                log.error("Can not find organization by identityId:{}", identityId);
                throw new BusinessException(RespCodeEnum.BIZ_EXCEPTION, ErrorMsg.ORGANIZATION_NOT_EXIST.getMsg());
            }

            ManagedChannel channel = assemblyChannel(orgExpand.getIdentityIp(), orgExpand.getIdentityPort());

            return channel;
        });
    }

    @Override
    public List<String> getEffectiveIdentityIdList() {
        LambdaQueryWrapper<Org> wrapper = Wrappers.lambdaQuery();
        wrapper.select(Org::getIdentityId).eq(Org::getStatus, OrgStatusEnum.Normal);
        return orgManager.listObjs(wrapper, Object::toString);
    }

    @Override
    public boolean isEffective(String identityId) {
        LambdaQueryWrapper<Org> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Org::getIdentityId, identityId);
        wrapper.eq(Org::getStatus, OrgStatusEnum.Normal);
        return orgManager.count(wrapper) == 1;
    }

    @Override
    public boolean isEffectiveAll(Set<String> identityIdList) {
        LambdaQueryWrapper<Org> wrapper = Wrappers.lambdaQuery();
        wrapper.in(Org::getIdentityId, identityIdList);
        wrapper.eq(Org::getStatus, OrgStatusEnum.Normal);
        return orgManager.count(wrapper) == identityIdList.size();
    }

    @Override
    public List<Org> getOrgListByIdentityIdList(Set<String> identityIdList) {
        LambdaQueryWrapper<Org> wrapper = Wrappers.lambdaQuery();
        wrapper.in(Org::getIdentityId, identityIdList);
        wrapper.eq(Org::getStatus, OrgStatusEnum.Normal);
        return orgManager.list(wrapper);
    }

    @Override
    public List<String> getIdentityIdListByUser(String hexAddress) {
        return userOrgManager.getIdentityIdListByUser(hexAddress);
    }

    @Override
    public boolean batchReplace(List<Org> orgList) {
        return orgManager.saveOrUpdateBatch(orgList);
    }

    @Override
    public List<Organization> getOrganizationListByUser() {
        UserDto userDto = commonService.getCurrentUser();
        return userOrgManager.getOrganizationListByUser(userDto.getAddress());
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void addOrganizationByUser(String identityIp, Integer identityPort) {
        // 查询组织信息
        ManagedChannel managedChannel = assemblyChannel(identityIp, identityPort);
        Empty empty = Empty.newBuilder().build();
        GetNodeIdentityResponse nodeIdentity = null;
        try {
            nodeIdentity = AuthServiceGrpc.newBlockingStub(managedChannel).withDeadlineAfter(10, TimeUnit.SECONDS).getNodeIdentity(empty);
        } catch (Exception e){
            log.error("AuthServiceClient->addUserOrganization() getNodeIdentity error",e);
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.ORGANIZATION_INFO_ERROR.getMsg());
        }

        if (nodeIdentity.getStatus() != GrpcConstant.GRPC_SUCCESS_CODE) {
            log.error("AuthServiceClient->getNodeIdentity() fail reason:{}", nodeIdentity.getMsg());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.ORGANIZATION_INFO_ERROR.getMsg());
        }
        // 添加或更新组织信息
        OrgExpand orgExpand = orgExpandManager.getById(nodeIdentity.getOwner().getIdentityId());
        if (null == orgExpand) {
            orgExpand = new OrgExpand();
            orgExpand.setIdentityId(nodeIdentity.getOwner().getIdentityId());
            orgExpand.setIdentityIp(identityIp);
            orgExpand.setIdentityPort(identityPort);
            orgExpand.setIsPublic(false);
            orgExpandManager.save(orgExpand);
        } else {
            if (!orgExpand.getIdentityIp().equals(identityIp) || orgExpand.getIdentityPort() != identityPort.intValue()) {
                orgExpand.setIdentityIp(identityIp);
                orgExpand.setIdentityPort(identityPort);
                orgExpandManager.updateById(orgExpand);
            }
        }
        // 绑定用户私有组织关系
        UserDto userDto = commonService.getCurrentUser();
        //获取原先的用户组织绑定关系信息，存在则更新，不存在则添加
        LambdaQueryWrapper<UserOrg> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserOrg::getOrgIdentityId, orgExpand.getIdentityId());
        wrapper.eq(UserOrg::getUserAddress, userDto.getAddress());
        UserOrg userOrg = userOrgManager.getOne(wrapper);
        if (null == userOrg) {
            userOrg = new UserOrg();
            userOrg.setUserAddress(userDto.getAddress());
            userOrg.setOrgIdentityId(orgExpand.getIdentityId());
            userOrgManager.save(userOrg);
        }
    }

    @Override
    public void deleteOrganizationByUser(String identityId) {
        // 删除用户组织关系
        UserDto userDto = commonService.getCurrentUser();
        LambdaUpdateWrapper<UserOrg> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(UserOrg::getUserAddress, userDto.getAddress());
        wrapper.eq(UserOrg::getOrgIdentityId, identityId);
        userOrgManager.remove(wrapper);
    }

    private ManagedChannel assemblyChannel(String identityIp, Integer identityPort){
        return ManagedChannelBuilder
                .forAddress(identityIp, identityPort)
                .usePlaintext()
                .keepAliveWithoutCalls(true)
                .build();
    }
}
