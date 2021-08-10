package com.platon.rosettaflow.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.dto.MemberRoleDto;
import com.platon.rosettaflow.mapper.MemberRoleMapper;
import com.platon.rosettaflow.mapper.domain.MemberRole;
import com.platon.rosettaflow.service.IMemberRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author admin
 * @date 2021/7/20
 */
@Slf4j
@Service
public class MemberRoleImpl extends ServiceImpl<MemberRoleMapper, MemberRole> implements IMemberRole {

    @Override
    public List<MemberRoleDto> getAll() {
        List<MemberRole> memberRoleList = this.list();
        return BeanUtil.copyToList(memberRoleList, MemberRoleDto.class);
    }
}
