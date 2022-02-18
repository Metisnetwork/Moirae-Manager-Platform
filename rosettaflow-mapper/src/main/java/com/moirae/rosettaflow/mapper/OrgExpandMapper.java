package com.moirae.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moirae.rosettaflow.mapper.domain.OrgExpand;

import java.util.List;

public interface OrgExpandMapper extends BaseMapper<OrgExpand> {

    List<String> getUsableIdentityIdList();
}
