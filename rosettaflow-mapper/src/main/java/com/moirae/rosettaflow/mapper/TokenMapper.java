package com.moirae.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moirae.rosettaflow.mapper.domain.Token;

/**
 * @author admin
 */
public interface TokenMapper extends BaseMapper<Token> {
    int countOfDataToken();
}
