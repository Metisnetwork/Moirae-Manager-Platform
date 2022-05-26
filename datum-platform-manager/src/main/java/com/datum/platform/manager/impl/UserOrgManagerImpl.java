package com.datum.platform.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datum.platform.manager.OrgUserManager;
import com.datum.platform.mapper.OrgUserMapper;
import com.datum.platform.mapper.domain.Org;
import com.datum.platform.mapper.domain.OrgUser;
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
    public List<Org> getUserOrgList(String address) {
        return baseMapper.getUserOrgList(address);
    }
}
