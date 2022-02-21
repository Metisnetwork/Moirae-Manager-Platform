package com.moirae.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moirae.rosettaflow.dto.OrganizationDto;
import com.moirae.rosettaflow.mapper.domain.Org;

public interface OrgMapper extends BaseMapper<Org> {

    IPage<OrganizationDto> listOrgInfoByNameOrderByTotalDataDesc(String keyword, Page<OrganizationDto> page);

    IPage<OrganizationDto> listOrgInfoByNameOrderByActivityDesc(String keyword, Page<OrganizationDto> page);
}
