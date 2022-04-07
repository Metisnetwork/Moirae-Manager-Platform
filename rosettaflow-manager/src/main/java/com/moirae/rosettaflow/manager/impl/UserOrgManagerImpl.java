package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.dto.OrganizationDto;
import com.moirae.rosettaflow.manager.OrgUserManager;
import com.moirae.rosettaflow.mapper.OrgUserMapper;
import com.moirae.rosettaflow.mapper.domain.Org;
import com.moirae.rosettaflow.mapper.domain.OrgUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserOrgManagerImpl extends ServiceImpl<OrgUserMapper, OrgUser> implements OrgUserManager {

    @Override
    public List<String> getIdentityIdListByUser(String address) {
        return baseMapper.getIdentityIdListByUser(address);
    }

    @Override
    public List<OrganizationDto> getOrganizationListByUser(String address) {
        return baseMapper.getOrganizationListByUser(address);
    }

    @Override
    public List<Org> getUserOrgList(String address) {
        return baseMapper.getUserOrgList(address);
    }
}
