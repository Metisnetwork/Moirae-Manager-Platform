package com.datum.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.datum.platform.mapper.domain.StatsMetaData;

import java.util.List;

/**
 * @author admin
 */
public interface StatsMetaDataMapper extends BaseMapper<StatsMetaData> {
    List<StatsMetaData> getMetaDataUsedTop(Integer size);
}
