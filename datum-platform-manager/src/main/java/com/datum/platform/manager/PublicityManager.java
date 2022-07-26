package com.datum.platform.manager;

import com.datum.platform.mapper.domain.Publicity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 公示信息 服务类
 * </p>
 *
 * @author chendai
 * @since 2022-07-08
 */
public interface PublicityManager extends IService<Publicity> {

    List<Publicity> listNeedSync();

    boolean saveBatch(Set<String> publicityIdSet);
}
