package com.datum.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.protobuf.Empty;
import com.datum.platform.chain.platon.contract.DatumNetworkPayContract;
import com.datum.platform.common.constants.SysConfig;
import com.datum.platform.common.enums.ErrorMsg;
import com.datum.platform.common.enums.OrgOrderByEnum;
import com.datum.platform.common.enums.RespCodeEnum;
import com.datum.platform.common.exception.BusinessException;
import com.datum.platform.dto.UserDto;
import com.datum.platform.grpc.constant.GrpcConstant;
import com.datum.platform.grpc.service.AuthServiceGrpc;
import com.datum.platform.grpc.service.GetNodeIdentityResponse;
import com.datum.platform.manager.OrgExpandManager;
import com.datum.platform.manager.OrgManager;
import com.datum.platform.manager.OrgUserManager;
import com.datum.platform.manager.StatsOrgManager;
import com.datum.platform.mapper.domain.Org;
import com.datum.platform.mapper.domain.OrgExpand;
import com.datum.platform.mapper.domain.OrgUser;
import com.datum.platform.mapper.domain.StatsOrg;
import com.datum.platform.service.OrgService;
import com.datum.platform.service.utils.UserContext;
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
    private DatumNetworkPayContract datumNetworkPayDao;

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
    public List<String> getIdentityIdListByUser(String hexAddress) {
        return userOrgManager.getIdentityIdListByUser(hexAddress);
    }

    @Override
    public boolean batchReplace(List<Org> orgList) {
        return orgManager.saveOrUpdateBatch(orgList);
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
    public Map<String, Org> getIdentityId2OrgMap() {
        return orgManager.list().stream().collect(Collectors.toMap(Org::getIdentityId, item -> item));
    }

    @Override
    public Org getOrgById(String identityId) {
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
        Set<String> whiteListSet = datumNetworkPayDao.whitelist(address).stream().collect(Collectors.toSet());
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
