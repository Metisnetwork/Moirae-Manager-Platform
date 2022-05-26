package com.datum.platform.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.datum.platform.mapper.domain.Org;
import com.datum.platform.mapper.domain.OrgUser;

import java.util.List;

public interface OrgUserManager extends IService<OrgUser> {

    List<String> getIdentityIdListByUser(String address);

    List<Org> getUserOrgList(String address);
}
