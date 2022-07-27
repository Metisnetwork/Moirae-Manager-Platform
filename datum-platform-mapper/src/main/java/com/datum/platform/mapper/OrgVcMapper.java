package com.datum.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.datum.platform.mapper.domain.OrgVc;

import java.util.List;

/**
 * <p>
 * 组织的VC Mapper 接口
 * </p>
 *
 * @author chendai
 * @since 2022-07-08
 */
public interface OrgVcMapper extends BaseMapper<OrgVc> {

    List<OrgVc> listLatest(Integer size);

    IPage<OrgVc> list(Page<OrgVc> page);
}
