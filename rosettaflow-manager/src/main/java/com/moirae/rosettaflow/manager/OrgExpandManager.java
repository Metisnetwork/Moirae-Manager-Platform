package com.moirae.rosettaflow.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.OrgExpand;

import java.util.List;

public interface OrgExpandManager extends IService<OrgExpand> {

    List<String> getUsableIdentityIdList();

    List<OrgExpand> getOrgExpandList();
}
