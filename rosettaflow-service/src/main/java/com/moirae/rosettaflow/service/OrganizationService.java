package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moirae.rosettaflow.mapper.domain.Org;
import com.moirae.rosettaflow.dto.OrganizationDto;
import io.grpc.ManagedChannel;

import java.util.List;
import java.util.Set;

public interface OrganizationService {

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
     * 获得可用的组织标识(状态正常)
     *
     * @return
     */
    List<String> getEffectiveIdentityIdList();

    /**
     * 获得可用并可连接的组织标识(状态正常 & 设置ip)
     *
     * @return
     */
    List<String> getUsableIdentityIdList();

    /**
     * 组织是否可用
     *
     * @param identityId
     * @return
     */
    boolean isEffective(String identityId);

    /**
     * 组织列表是否可用
     *
     * @param identityIdList
     * @return
     */
    boolean isEffectiveAll(Set<String> identityIdList);

    /**
     * 通过组织id列表查询组织信息
     *
     * @param identityIdList
     * @return
     */
    List<Org> getOrgListByIdentityIdList(Set<String> identityIdList);

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
     * 获得用户关联的组织
     *
     * @return
     */
    List<OrganizationDto> getOrganizationListByUser();


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

    /**
     * 查询组织列表
     *
     * @param current
     * @param size
     * @param keyword
     * @return
     */
    IPage<OrganizationDto> listOrgInfoByNameOrderByNameAsc(Long current, Long size, String keyword);

    /**
     * 查询组织列表
     *
     * @param current
     * @param size
     * @param keyword
     * @return
     */
    IPage<OrganizationDto> listOrgInfoByNameOrderByTotalDataDesc(Long current, Long size, String keyword);

    /**
     * 查询组织列表
     *
     * @param current
     * @param size
     * @param keyword
     * @return
     */
    IPage<OrganizationDto> listOrgInfoByNameOrderByActivityDesc(Long current, Long size, String keyword);
}
