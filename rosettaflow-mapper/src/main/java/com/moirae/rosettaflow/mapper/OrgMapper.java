package com.moirae.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moirae.rosettaflow.dto.OrganizationDto;
import com.moirae.rosettaflow.mapper.domain.Org;
import com.moirae.rosettaflow.mapper.domain.StatsOrg;

import java.util.List;

public interface OrgMapper extends BaseMapper<Org> {

    IPage<OrganizationDto> listOrgInfoByNameOrderByTotalDataDesc(String keyword, Page<OrganizationDto> page);

    IPage<OrganizationDto> listOrgInfoByNameOrderByActivityDesc(String keyword, Page<OrganizationDto> page);

    IPage<OrganizationDto> listOrgInfoByNameOrderByMemoryDesc(String keyword, Page<OrganizationDto> page);

    OrganizationDto findOrgInfoDetail(String identityId);

    IPage<Org> getOrgList(Page<Org> page, String keyword, String orderBy);

    Org getOrgDetails(String identityId);

    StatsOrg getStatsOrg(String identityId);
}
