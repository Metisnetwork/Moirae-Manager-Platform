package com.moirae.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.protobuf.Empty;
import com.moirae.rosettaflow.chain.platon.contract.MetisPayContract;
import com.moirae.rosettaflow.common.constants.SysConfig;
import com.moirae.rosettaflow.common.enums.ErrorMsg;
import com.moirae.rosettaflow.common.enums.OrgOrderByEnum;
import com.moirae.rosettaflow.common.enums.RespCodeEnum;
import com.moirae.rosettaflow.common.exception.BusinessException;
import com.moirae.rosettaflow.dto.OrganizationDto;
import com.moirae.rosettaflow.dto.UserDto;
import com.moirae.rosettaflow.grpc.constant.GrpcConstant;
import com.moirae.rosettaflow.grpc.service.AuthServiceGrpc;
import com.moirae.rosettaflow.grpc.service.GetNodeIdentityResponse;
import com.moirae.rosettaflow.manager.OrgExpandManager;
import com.moirae.rosettaflow.manager.OrgManager;
import com.moirae.rosettaflow.manager.OrgUserManager;
import com.moirae.rosettaflow.manager.StatsOrgManager;
import com.moirae.rosettaflow.mapper.domain.Org;
import com.moirae.rosettaflow.mapper.domain.OrgExpand;
import com.moirae.rosettaflow.mapper.domain.OrgUser;
import com.moirae.rosettaflow.mapper.domain.StatsOrg;
import com.moirae.rosettaflow.mapper.enums.OrgStatusEnum;
import com.moirae.rosettaflow.service.OrgService;
import com.moirae.rosettaflow.service.utils.UserContext;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
public class OrgServiceImpl implements OrgService {

    @Resource
    private SysConfig sysConfig;
    @Resource
    private OrgExpandManager orgExpandManager;
    @Resource
    private OrgManager orgManager;
    @Resource
    private OrgUserManager userOrgManager;
    @Resource
    private StatsOrgManager statsOrgManager;
    @Resource
    private MetisPayContract metisPayDao;

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
    public List<String> getUsableIdentityIdList() {
        return orgExpandManager.getUsableIdentityIdList();
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
    public List<OrganizationDto> getOrganizationListByUser() {
        UserDto userDto = UserContext.getCurrentUser();
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
            log.error("GrpcAuthServiceClientImpl->addUserOrganization() getNodeIdentity error",e);
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.ORGANIZATION_INFO_ERROR.getMsg());
        }

        if (nodeIdentity.getStatus() != GrpcConstant.GRPC_SUCCESS_CODE) {
            log.error("GrpcAuthServiceClientImpl->getNodeIdentity() fail reason:{}", nodeIdentity.getMsg());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.ORGANIZATION_INFO_ERROR.getMsg());
        }
        // 添加或更新组织信息
        OrgExpand orgExpand = orgExpandManager.getById(nodeIdentity.getInformation().getIdentityId());
        if (null == orgExpand) {
            orgExpand = new OrgExpand();
            orgExpand.setIdentityId(nodeIdentity.getInformation().getIdentityId());
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
        UserDto userDto = UserContext.getCurrentUser();
        //获取原先的用户组织绑定关系信息，存在则更新，不存在则添加
        LambdaQueryWrapper<OrgUser> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(OrgUser::getIdentityId, orgExpand.getIdentityId());
        wrapper.eq(OrgUser::getAddress, userDto.getAddress());
        OrgUser userOrg = userOrgManager.getOne(wrapper);
        if (null == userOrg) {
            userOrg = new OrgUser();
            userOrg.setAddress(userDto.getAddress());
            userOrg.setIdentityId(orgExpand.getIdentityId());
            userOrgManager.save(userOrg);
        }
    }

    @Override
    public void deleteOrganizationByUser(String identityId) {
        // 删除用户组织关系
        UserDto userDto = UserContext.getCurrentUser();
        LambdaUpdateWrapper<OrgUser> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(OrgUser::getAddress, userDto.getAddress());
        wrapper.eq(OrgUser::getIdentityId, identityId);
        userOrgManager.remove(wrapper);
    }

    @Override
    public IPage<OrganizationDto> listOrgInfoByNameOrderByNameAsc(Long current, Long size, String keyword) {
        Page<Org> page = new Page<>(current, size);
        LambdaQueryWrapper<Org> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Org::getStatus, OrgStatusEnum.Normal);
        wrapper.like(StringUtils.isNotBlank(keyword), Org::getNodeName, keyword);
        wrapper.orderByAsc(Org::getNodeName);
        orgManager.page(page, wrapper);

        Page<OrganizationDto> organizationPage = new Page<>();
        organizationPage.setCurrent(page.getCurrent());
        organizationPage.setSize(page.getSize());
        organizationPage.setTotal(page.getTotal());
        organizationPage.setRecords(page.getRecords().stream()
                .map(item -> {
                    OrganizationDto organization = new OrganizationDto();
                    organization.setIdentityId(item.getIdentityId());
                    organization.setNodeName(item.getNodeName());
                    organization.setStatus(item.getStatus());
                    organization.setUpdateAt(item.getUpdateAt());
                    return organization;
                })
                .collect(Collectors.toList())
        );
        return organizationPage;
    }

    @Override
    public IPage<OrganizationDto> listOrgInfoByNameOrderByTotalDataDesc(Long current, Long size, String keyword) {
        Page<OrganizationDto> page = new Page<>(current, size);
        orgManager.listOrgInfoByNameOrderByTotalDataDesc(page, keyword);
        return page;
    }

    @Override
    public IPage<OrganizationDto> listOrgInfoByNameOrderByActivityDesc(Long current, Long size, String keyword) {
        Page<OrganizationDto> page = new Page<>(current, size);
        orgManager.listOrgInfoByNameOrderByActivityDesc(page, keyword);
        return page;
    }

    @Override
    public IPage<OrganizationDto> listOrgInfoByNameOrderByMemoryDesc(Long current, Long size, String keyword) {
        Page<OrganizationDto> page = new Page<>(current, size);
        orgManager.listOrgInfoByNameOrderByMemoryDesc(page, keyword);
        return page;
    }

    @Override
    public OrganizationDto findOrgInfoDetail(String identityId) {
        return orgManager.findOrgInfoDetail(identityId);
    }

    @Override
    public Map<String, Org> getIdentityId2OrgMap() {
        return orgManager.list().stream().collect(Collectors.toMap(Org::getIdentityId, item -> item));
    }

    @Override
    public Org findOrgById(String identityId) {
        return orgManager.getById(identityId);
    }

    @Override
    public int getOrgStats() {
        return orgManager.getOrgStats();
    }

    @Override
    public IPage<Org> getOrgList(Long current, Long size, String keyword, OrgOrderByEnum orderBy) {
        Page<Org> page = new Page<>(current, size);
        if(orderBy == null){
            orderBy = OrgOrderByEnum.NAME;
        }
        orgManager.getOrgList(page, keyword, orderBy.getSqlValue());
        return page;
    }

    @Override
    public Org getOrgDetails(String identityId) {
        return orgManager.getOrgDetails(identityId);
    }

    @Override
    public List<OrgExpand> getOrgExpandList() {
        return orgExpandManager.getOrgExpandList();
    }

    @Override
    public void batchUpdateOrgExpand(List<OrgExpand> updateList) {
        orgExpandManager.updateBatchById(updateList);
    }

    @Override
    public List<String> getEffectiveOrgIdList() {
        return orgManager.getEffectiveOrgIdList();
    }

    @Override
    public void batchInsertOrUpdateStatsOrg(List<StatsOrg> saveList) {
        statsOrgManager.saveOrUpdateBatch(saveList);
    }

    @Override
    public StatsOrg getStatsOrg(String identityId) {
        return orgManager.getStatsOrg(identityId);
    }

    @Override
    public List<Org> getUserOrgList() {
        String address = UserContext.getCurrentUser().getAddress();
        List<Org> orgList = userOrgManager.getUserOrgList(address);
        Set<String> whiteListSet = metisPayDao.whitelist(address).stream().collect(Collectors.toSet());
        orgList.forEach(item -> {
            item.setIsInWhitelist(whiteListSet.contains(item.getObserverProxyWalletAddress())?true:false);
        });
        return orgList;
    }

    private ManagedChannel assemblyChannel(String identityIp, Integer identityPort){
        return ManagedChannelBuilder
                .forAddress(identityIp, identityPort)
                .usePlaintext()
                .keepAliveWithoutCalls(true)
                .build();
    }
}
