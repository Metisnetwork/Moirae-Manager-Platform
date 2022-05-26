package com.datum.platform.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.datum.platform.mapper.domain.Org;
import com.datum.platform.mapper.domain.StatsOrg;

import java.util.List;

public interface OrgManager extends IService<Org> {

    int getOrgStats();

    IPage<Org> getOrgList(Page<Org> page, String keyword, String orderBy);

    Org getOrgDetails(String identityId);

    List<String> getEffectiveOrgIdList();

    StatsOrg getStatsOrg(String identityId);
}
