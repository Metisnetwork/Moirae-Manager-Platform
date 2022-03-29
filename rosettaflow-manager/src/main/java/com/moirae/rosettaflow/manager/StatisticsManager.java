package com.moirae.rosettaflow.manager;

import com.moirae.rosettaflow.mapper.domain.StatsGlobal;
import com.moirae.rosettaflow.mapper.domain.StatisticsDataTrend;

import java.util.List;

public interface StatisticsManager {

    List<StatisticsDataTrend> listGlobalDataFileStatsTrendMonthly();

    StatsGlobal globalStats();
}
