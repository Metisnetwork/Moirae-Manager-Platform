package com.datum.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.datum.platform.mapper.domain.Org;
import com.datum.platform.mapper.domain.StatsOrg;

public interface OrgMapper extends BaseMapper<Org> {

    IPage<Org> getOrgList(Page<Org> page, String keyword, String orderBy);

    Org getOrgDetails(String identityId);

    StatsOrg getStatsOrg(String identityId);
}
