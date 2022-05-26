package com.datum.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.datum.platform.mapper.domain.Token;

/**
 * @author admin
 */
public interface TokenMapper extends BaseMapper<Token> {
    int countOfDataToken();
}
