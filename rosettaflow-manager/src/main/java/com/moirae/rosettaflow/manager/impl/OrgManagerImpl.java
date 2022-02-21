package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.dto.OrganizationDto;
import com.moirae.rosettaflow.manager.OrgManager;
import com.moirae.rosettaflow.mapper.OrgMapper;
import com.moirae.rosettaflow.mapper.domain.Org;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrgManagerImpl extends ServiceImpl<OrgMapper, Org> implements OrgManager {

    @Override
    public IPage<OrganizationDto> listOrgInfoByNameOrderByTotalDataDesc(Page<OrganizationDto> page, String keyword) {
        return this.baseMapper.listOrgInfoByNameOrderByTotalDataDesc(keyword, page);
    }

    @Override
    public IPage<OrganizationDto> listOrgInfoByNameOrderByActivityDesc(Page<OrganizationDto> page, String keyword) {
        return this.baseMapper.listOrgInfoByNameOrderByActivityDesc(keyword, page);
    }
}
