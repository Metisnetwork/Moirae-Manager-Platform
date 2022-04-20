package com.moirae.rosettaflow.mapper;

import com.moirae.rosettaflow.mapper.domain.StatisticsDataTrend;
import com.moirae.rosettaflow.mapper.domain.StatsGlobal;

import java.util.List;

public interface StatisticsMapper {

    List<StatisticsDataTrend> listGlobalDataFileStatsTrendMonthly();

    StatsGlobal globalStats();
}
