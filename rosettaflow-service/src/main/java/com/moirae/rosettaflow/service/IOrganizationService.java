package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.Organization;

import java.util.List;
import java.util.Set;

/**
 * @author hudenian
 * @date 2021/9/26
 * @description 机构服务实现类
 */
public interface IOrganizationService extends IService<Organization> {
    /**
     * 批量插入机构信息
     *
     * @param organizationList 机构列表
     */
    void batchInsert(List<Organization> organizationList);

    /**
     * 根据identityId获取机构信息
     *
     * @param identityId identityId
     * @return 机构信息
     */
    Organization getByIdentityId(String identityId);

    /**
     * 根据identityId列表获取对应机构集合
     *
     * @param identityArr identityId数组
     * @return 组织列表
     */
    List<Organization> getByIdentityIds(Object[] identityArr);

    /**
     * 获取全部机构
     *
     * @return 组织列表
     */
    List<Organization> getAllIdentity();

    /**
     * 查询用户的组织信息
     *
     * @param hexAddress
     * @return
     */
    List<Organization> getAllByUser(String hexAddress);

    /**
     * 查询用户的组织信息
     *
     * @return
     */
    List<Organization> getAllByUserSession();


    /**
     * 添加用户组织
     *
     * @param identityIp
     * @param identityPort
     */
    void addUserOrganization(String identityIp, Integer identityPort);

    /**
     * 删除用户组织
     *
     * @param identityId
     */
    void deleteUserOrganization(String identityId);

    void isValid(Set<String> senderOrgId);
}
