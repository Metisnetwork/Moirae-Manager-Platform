package com.datum.platform.service;


import com.datum.platform.mapper.domain.StatsDay;
import com.datum.platform.mapper.domain.StatsGlobal;
import com.datum.platform.mapper.domain.StatsMetaData;
import com.datum.platform.mapper.domain.StatsOrg;
import com.datum.platform.service.dto.statistics.NavigationDto;

import java.util.Collection;
import java.util.List;

public interface StatisticsService {

    /**
     * 首页导航
     *
     * @param keyword  任务id 或 组织标识
     * @return
     */
    NavigationDto queryNavigation(String keyword);

    /**
     * 全网统计
     *
     * @return
     */
    StatsGlobal globalStats();

    List<StatsDay> getTaskTrend(Integer size);

    List<StatsOrg> getOrgComputingTop(Integer size);

    List<StatsMetaData> getMetaDataUsedTop(Integer size);

    boolean updateStatsGlobal(StatsGlobal global);

    StatsMetaData getStatsMetaDataById(String metaDataId);

    boolean saveOrUpdateBatchStatsMetaData(Collection<StatsMetaData> statsMetaDataList);
}
