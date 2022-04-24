package com.moirae.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moirae.rosettaflow.mapper.domain.StatsData;

import java.util.List;

/**
 * @author admin
 */
public interface StatsDataMapper extends BaseMapper<StatsData> {
    List<StatsData> getDataTokenUsedTop(Integer size);
}
