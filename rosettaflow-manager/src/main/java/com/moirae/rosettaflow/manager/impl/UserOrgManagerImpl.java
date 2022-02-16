package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.manager.UserOrgManager;
import com.moirae.rosettaflow.mapper.UserOrgMapper;
import com.moirae.rosettaflow.mapper.domain.Organization;
import com.moirae.rosettaflow.mapper.domain.UserOrg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserOrgManagerImpl extends ServiceImpl<UserOrgMapper, UserOrg> implements UserOrgManager {

    @Override
    public List<String> getIdentityIdListByUser(String address) {
        return baseMapper.getIdentityIdListByUser(address);
    }

    @Override
    public List<Organization> getOrganizationListByUser(String address) {
        return baseMapper.getOrganizationListByUser(address);
    }
}
