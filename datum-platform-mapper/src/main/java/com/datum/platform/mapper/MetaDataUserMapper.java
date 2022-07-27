package com.datum.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.datum.platform.mapper.domain.MetaDataUser;

import java.util.List;

/**
 * <p>
 * 用户可见的元数据 Mapper 接口
 * </p>
 *
 * @author chendai
 * @since 2022-06-28
 */
public interface MetaDataUserMapper extends BaseMapper<MetaDataUser> {

    boolean saveBatch(List<MetaDataUser> addList);
}
