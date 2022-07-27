package com.datum.platform.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.datum.platform.mapper.domain.OrgVc;

import java.util.List;

/**
 * <p>
 * 组织的VC 服务类
 * </p>
 *
 * @author chendai
 * @since 2022-07-08
 */
public interface OrgVcManager extends IService<OrgVc> {

    List<String> listId();

    List<OrgVc> listLatest(Integer size);

    IPage<OrgVc> list(Page<OrgVc> page);
}
