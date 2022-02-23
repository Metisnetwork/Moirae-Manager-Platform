package com.moirae.rosettaflow.service;


import com.moirae.rosettaflow.mapper.domain.GlobalStats;
import com.moirae.rosettaflow.mapper.domain.StatisticsDataTrend;

import java.util.List;

public interface StatisticsService {
    List<StatisticsDataTrend> listGlobalDataFileStatsTrendMonthly();

    GlobalStats globalStats();
}
