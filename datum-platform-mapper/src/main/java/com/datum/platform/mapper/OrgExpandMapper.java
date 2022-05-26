package com.datum.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.datum.platform.mapper.domain.OrgExpand;

import java.util.List;

public interface OrgExpandMapper extends BaseMapper<OrgExpand> {

    List<String> getUsableIdentityIdList();
}
