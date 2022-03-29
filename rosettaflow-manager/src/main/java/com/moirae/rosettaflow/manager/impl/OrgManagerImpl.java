package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.dto.OrganizationDto;
import com.moirae.rosettaflow.manager.OrgManager;
import com.moirae.rosettaflow.mapper.OrgMapper;
import com.moirae.rosettaflow.mapper.domain.MetaData;
import com.moirae.rosettaflow.mapper.domain.Org;
import com.moirae.rosettaflow.mapper.enums.OrgStatusEnum;
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

    @Override
    public IPage<OrganizationDto> listOrgInfoByNameOrderByMemoryDesc(Page<OrganizationDto> page, String keyword) {
        return this.baseMapper.listOrgInfoByNameOrderByMemoryDesc(keyword, page);
    }

    @Override
    public OrganizationDto findOrgInfoDetail(String identityId) {
        return this.baseMapper.findOrgInfoDetail(identityId);
    }

    @Override
    public int getOrgStats() {
        LambdaQueryWrapper<Org> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Org::getStatus, OrgStatusEnum.Normal);
        return count(wrapper);
    }

    @Override
    public IPage<Org> getOrgList(Page<MetaData> page, String keyword, String orderBy) {
        return this.baseMapper.getOrgList(page, keyword, orderBy);
    }

    @Override
    public Org getOrgDetails(String identityId) {
        return this.baseMapper.getOrgDetails(identityId);
    }
}
