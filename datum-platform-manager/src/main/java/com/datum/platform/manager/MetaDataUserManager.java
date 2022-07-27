package com.datum.platform.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.datum.platform.mapper.domain.MetaDataUser;

import java.util.List;

/**
 * <p>
 * 用户可见的元数据 服务类
 * </p>
 *
 * @author chendai
 * @since 2022-06-28
 */
public interface MetaDataUserManager extends IService<MetaDataUser> {

    boolean saveOrDeleteBatch(String address, List<String> metaDataIdList);
}
