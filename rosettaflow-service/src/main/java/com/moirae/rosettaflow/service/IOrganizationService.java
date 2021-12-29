package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.Organization;

import java.util.List;

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
     * 获取所有公共组织
     * @return 机构信息
     */
    List<Organization> getAllPublicByIdentity();

    /**
     * 根据组织id和公共标志，获取某个公共组织
     * @param identityId identityId
     * @param publicFlag 是否公有可查节点: 0-否，1- 是
     * @return 机构信息
     */
    Organization getByIdentityIAndPublicFlag(String identityId, Byte publicFlag);

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
     * 删除组织，根据组织id
     * @param identityId
     * @return Boolean
     */
    Boolean deleteByIdentityId(String identityId);
}
