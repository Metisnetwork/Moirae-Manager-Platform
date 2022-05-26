package com.datum.platform.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.datum.platform.mapper.domain.OrgExpand;

import java.util.List;

public interface OrgExpandManager extends IService<OrgExpand> {

    List<OrgExpand> getOrgExpandList();
}
