package com.datum.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.datum.platform.mapper.domain.TokenHolder;

import java.util.List;

/**
 * @author admin
 */
public interface TokenHolderMapper extends BaseMapper<TokenHolder> {
    boolean updateBatch(List<TokenHolder> updateList);
}
