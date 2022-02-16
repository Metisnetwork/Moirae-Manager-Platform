package com.moirae.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moirae.rosettaflow.mapper.domain.Organization;
import com.moirae.rosettaflow.mapper.domain.UserOrg;

import java.util.List;

/**
 * @author admin
 */
public interface UserOrgMapper extends BaseMapper<UserOrg> {

    List<String> getIdentityIdListByUser(String address);

    List<Organization> getOrganizationListByUser(String address);
}
