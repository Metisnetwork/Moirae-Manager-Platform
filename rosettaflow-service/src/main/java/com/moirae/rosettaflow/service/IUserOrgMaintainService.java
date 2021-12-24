package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.dto.UserOrgMaintainDto;
import com.moirae.rosettaflow.mapper.domain.UserOrgMaintain;

/**
 * 用户组织连接绑定关系服务类
 *
 * @author hudenian
 * @date 2021/12/15
 */
public interface IUserOrgMaintainService extends IService<UserOrgMaintain> {

    /**
     * 增加用户与组织及ip绑定关系
     *
     * @param identityIp   组织ip
     * @param identityPort 组织端口
     */
    void ipPortBind(String identityIp, Integer identityPort);

    /**
     * 根据用户地址及组织identityId获取用户组织绑定关系信息
     *
     * @param address    用户钱包地址
     * @param identityId 组织identityId
     * @return 用户组织绑定关系信息
     */
    UserOrgMaintain getByAddressAndIdentityId(String address, String identityId);

    /**
     * 将用户组织连接绑定关系表中identityId一样 ip与port不一样的数据更新成失败
     *
     * @param identityId   identityId
     * @param identityIp   组织ip
     * @param identityPort 组织端口
     */
    void updateValidFlag(String identityId, String identityIp, Integer identityPort);

    /**
     * 查询当前用户维护的组织绑定信息关系表
     *
     * @param orgName 组织名称
     * @param current 当前页
     * @param size    每页大小
     * @return 用户维护组织关系分页列表
     */
    IPage<UserOrgMaintainDto> queryUserOrgMaintainPageList(String orgName, Long current, Long size);

    /**
     * 连接组织
     *
     * @param identityId 组织id
     */
    void connectIdentity(String identityId);

    /**
     * 断开连接组织
     *
     * @param identityId 组织id
     */
    void disconnectIdentity(String identityId);

    /**
     * 删除用户与组织ip及port绑定关系
     *
     * @param id 用户组织连接绑定关系表id
     */
    void delIpPortBind(Long id);

}
