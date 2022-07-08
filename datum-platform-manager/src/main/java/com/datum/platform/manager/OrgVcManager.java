package com.datum.platform.manager;

import com.datum.platform.mapper.domain.OrgVc;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
