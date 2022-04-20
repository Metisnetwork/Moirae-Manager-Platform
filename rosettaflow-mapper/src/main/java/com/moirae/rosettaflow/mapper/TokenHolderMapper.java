package com.moirae.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moirae.rosettaflow.mapper.domain.TokenHolder;

import java.util.List;

/**
 * @author admin
 */
public interface TokenHolderMapper extends BaseMapper<TokenHolder> {
    boolean updateBatch(List<TokenHolder> updateList);
}
