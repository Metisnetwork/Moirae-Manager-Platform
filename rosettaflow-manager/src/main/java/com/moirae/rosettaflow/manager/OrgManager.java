package com.moirae.rosettaflow.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.dto.OrganizationDto;
import com.moirae.rosettaflow.mapper.domain.Org;
import com.moirae.rosettaflow.mapper.domain.StatsOrg;

import java.util.List;

public interface OrgManager extends IService<Org> {

    IPage<OrganizationDto> listOrgInfoByNameOrderByTotalDataDesc(Page<OrganizationDto> page, String keyword);

    IPage<OrganizationDto> listOrgInfoByNameOrderByActivityDesc(Page<OrganizationDto> page, String keyword);

    IPage<OrganizationDto> listOrgInfoByNameOrderByMemoryDesc(Page<OrganizationDto> page, String keyword);

    OrganizationDto findOrgInfoDetail(String identityId);

    int getOrgStats();

    IPage<Org> getOrgList(Page<Org> page, String keyword, String orderBy);

    Org getOrgDetails(String identityId);

    List<String> getEffectiveOrgIdList();

    StatsOrg getStatsOrg(String identityId);
}
