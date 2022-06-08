package com.datum.platform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.datum.platform.common.enums.OrgOrderByEnum;
import com.datum.platform.mapper.domain.Org;
import com.datum.platform.mapper.domain.OrgExpand;
import com.datum.platform.mapper.domain.StatsOrg;
import io.grpc.ManagedChannel;

import java.util.List;
import java.util.Map;

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
     * @param orgList
     * @return
     */
    boolean batchReplace(List<Org> orgList);

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

    List<OrgExpand> getOrgExpandList();

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

    List<Org> getBaseOrgList();
}
