package com.platon.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platon.rosettaflow.dto.MemberRoleDto;
import com.platon.rosettaflow.mapper.domain.MemberRole;

import java.util.List;

/**
 * @author admin
 * @date 2021/7/20
 */
public interface IMemberRole extends IService<MemberRole> {

    /**
     * 获取所角色
     *
     * @return 角色列表
     */
    List<MemberRoleDto> getAll();
}
