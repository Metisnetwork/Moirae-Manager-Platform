package com.datum.platform.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.datum.platform.mapper.domain.OrgExpand;

import java.util.List;

public interface OrgExpandManager extends IService<OrgExpand> {
    boolean saveOfNotExist(List<OrgExpand> orgExpandList);

    List<OrgExpand> listHaveIpPort();

    IPage<OrgExpand> list(Page<OrgExpand> page);

    Integer countOfAuthority();
}
