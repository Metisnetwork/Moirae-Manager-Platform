package com.moirae.rosettaflow.service;


import com.moirae.rosettaflow.mapper.domain.StatsData;
import com.moirae.rosettaflow.mapper.domain.StatsDay;
import com.moirae.rosettaflow.mapper.domain.StatsGlobal;
import com.moirae.rosettaflow.mapper.domain.StatsOrg;
import com.moirae.rosettaflow.service.dto.statistics.NavigationDto;

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

    List<StatsData> getDataTokenUsedTop(Integer size);
}
