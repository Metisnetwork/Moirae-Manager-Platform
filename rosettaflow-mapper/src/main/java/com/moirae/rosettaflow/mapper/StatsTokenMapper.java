package com.moirae.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moirae.rosettaflow.mapper.domain.StatsToken;

import java.util.List;

/**
 * @author admin
 */
public interface StatsTokenMapper extends BaseMapper<StatsToken> {
    List<StatsToken> getDataTokenUsedTop(Integer size);
}
