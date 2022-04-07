package com.moirae.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moirae.rosettaflow.dto.OrganizationDto;
import com.moirae.rosettaflow.mapper.domain.Org;
import com.moirae.rosettaflow.mapper.domain.OrgUser;

import java.util.List;

/**
 * @author admin
 */
public interface OrgUserMapper extends BaseMapper<OrgUser> {

    List<String> getIdentityIdListByUser(String address);

    List<OrganizationDto> getOrganizationListByUser(String address);

    List<Org> getUserOrgList(String address);
}
