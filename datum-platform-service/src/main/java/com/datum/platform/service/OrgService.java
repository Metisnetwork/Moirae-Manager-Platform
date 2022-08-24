package com.datum.platform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.datum.platform.common.enums.OrgOrderByEnum;
import com.datum.platform.mapper.domain.*;
import io.grpc.ManagedChannel;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface OrgService {

    /**
     * 通过配置文件初始公共组织
     */
    void initPublicOrg();

    /**
     * 获取grpc的channel
     *
     * @param identityId
     * @return
     */
    ManagedChannel getChannel(String identityId);

    /**
     * 获得用户关联的组织标识
     *
     * @param hexAddress
     * @return
     */
    List<String> getIdentityIdListByUser(String hexAddress);

    /**
     * 批量提供组织信息
     *
     */
    boolean batchReplace(List<Org> orgList,  List<String> addOrgIdList, List<OrgVc> orgVcList, Set<String> publicityIdSet);

    /**
     * 用户添加私有组织
     *
     * @param identityIp
     * @param identityPort
     */
    void addOrganizationByUser(String identityIp, Integer identityPort);


    /**
     * 用户数删除私有组织
     *
     * @param identityId
     */
    void deleteOrganizationByUser(String identityId);

    Map<String, Org> getIdentityId2OrgMap();

    Org getOrgById(String identityId);

    int getOrgStats();

    IPage<Org> getOrgList(Long current, Long size, String keyword, OrgOrderByEnum orderBy);

    Org getOrgDetails(String identityId);

    void batchUpdateOrgExpand(List<OrgExpand> updateList);

    List<String> getEffectiveOrgIdList();

    void batchInsertOrUpdateStatsOrg(List<StatsOrg> saveList);

    StatsOrg getStatsOrg(String identityId);

    /**
     * 查询用户可用的组织列表
     *
     * @return
     */
    List<Org> getUserOrgList();

    List<Org> getBaseOrgList(Integer algorithmType);

    List<OrgExpand> listOrgExpand();

    List<String> listOrgVcId();

    List<OrgExpand> listHaveIpPortOrgExpand();

    boolean batchSaveOrgVc(List<OrgVc> orgVcList, List<Publicity> publicityList);

    int countOfOrgVc();

    List<OrgVc> getLatestOrgVcList(Integer size);

    IPage<OrgVc> listOrgVcList(Long current, Long size);

    IPage<OrgExpand> listAuthority(Long current, Long size);

    Integer countOfAuthority();

    List<Org> getPowerOrgList();

    List<OrgVc> listNeedVerifyOrgVc();

    void verifyOrgVcFinish(String identityId,int status);
}
