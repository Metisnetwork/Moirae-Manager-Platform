package com.datum.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.datum.platform.mapper.domain.Org;
import com.datum.platform.mapper.domain.OrgUser;

import java.util.List;

/**
 * @author admin
 */
public interface OrgUserMapper extends BaseMapper<OrgUser> {

    List<String> getIdentityIdListByUser(String address);

    List<Org> getUserOrgList(String address);
}
