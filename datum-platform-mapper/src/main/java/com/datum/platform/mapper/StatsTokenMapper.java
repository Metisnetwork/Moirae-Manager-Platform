package com.datum.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.datum.platform.mapper.domain.StatsToken;

import java.util.List;

/**
 * @author admin
 */
public interface StatsTokenMapper extends BaseMapper<StatsToken> {
    List<StatsToken> getDataTokenUsedTop(Integer size);
}
