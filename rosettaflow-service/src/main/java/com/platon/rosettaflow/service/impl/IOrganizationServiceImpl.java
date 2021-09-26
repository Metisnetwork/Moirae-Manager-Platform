package com.platon.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.common.enums.StatusEnum;
import com.platon.rosettaflow.mapper.OrganizationMapper;
import com.platon.rosettaflow.mapper.domain.Organization;
import com.platon.rosettaflow.service.IOrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author hudenian
 * @date 2021/9/26
 * @description 机构服务实现类
 */
@Slf4j
@Service
public class IOrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization> implements IOrganizationService {
    @Override
    public void batchInsert(List<Organization> organizationList) {
        this.baseMapper.batchInsert(organizationList);
    }

    @Override
    public Organization getByIdentityId(String identityId) {
        LambdaQueryWrapper<Organization> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Organization::getIdentityId, identityId);
        wrapper.eq(Organization::getStatus, StatusEnum.VALID.getValue());
        return this.getOne(wrapper);
    }

    @Override
    public Map<String, Organization> getByIdentityIds(Object[] identityArr) {
        LambdaQueryWrapper<Organization> wrapper = Wrappers.lambdaQuery();
        wrapper.in(Organization::getIdentityId, identityArr);
        return this.list(wrapper).stream().collect(Collectors.toMap(Organization::getIdentityId, organization -> organization));
    }
}
