package com.datum.platform.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datum.platform.manager.OrgManager;
import com.datum.platform.mapper.OrgMapper;
import com.datum.platform.mapper.domain.Org;
import com.datum.platform.mapper.domain.StatsOrg;
import com.datum.platform.mapper.enums.OrgStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class OrgManagerImpl extends ServiceImpl<OrgMapper, Org> implements OrgManager {

    @Override
    public int getOrgStats() {
        LambdaQueryWrapper<Org> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Org::getStatus, OrgStatusEnum.Normal);
        return count(wrapper);
    }

    @Override
    public IPage<Org> getOrgList(Page<Org> page, String keyword, String orderBy) {
        return this.baseMapper.getOrgList(page, keyword, orderBy);
    }

    @Override
    public Org getOrgDetails(String identityId) {
        return this.baseMapper.getOrgDetails(identityId);
    }

    @Override
    public List<String> getEffectiveOrgIdList() {
        LambdaQueryWrapper<Org> wrapper = Wrappers.lambdaQuery();
        wrapper.select(Org::getIdentityId);
        wrapper.eq(Org::getStatus, OrgStatusEnum.Normal);
        return listObjs(wrapper, item -> item.toString());
    }

    @Override
    public StatsOrg getStatsOrg(String identityId) {
        return baseMapper.getStatsOrg(identityId);
    }
}
