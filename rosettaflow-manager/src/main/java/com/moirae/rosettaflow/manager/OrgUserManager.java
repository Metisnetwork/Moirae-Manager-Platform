package com.moirae.rosettaflow.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.dto.OrganizationDto;
import com.moirae.rosettaflow.mapper.domain.Org;
import com.moirae.rosettaflow.mapper.domain.OrgUser;

import java.util.List;

public interface OrgUserManager extends IService<OrgUser> {

    List<String> getIdentityIdListByUser(String address);

    List<OrganizationDto> getOrganizationListByUser(String address);

    List<Org> getUserOrgList(String address);
}
