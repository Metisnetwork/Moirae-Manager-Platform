package com.datum.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.datum.platform.mapper.domain.OrgExpand;

import java.util.List;

public interface OrgExpandMapper extends BaseMapper<OrgExpand> {

    List<String> getUsableIdentityIdList();

    IPage<OrgExpand> list(Page<OrgExpand> page);

    Integer countOfAuthority();
}
