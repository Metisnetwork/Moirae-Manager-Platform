package com.moirae.rosettaflow.service.impl;

import com.moirae.rosettaflow.manager.StatisticsManager;
import com.moirae.rosettaflow.mapper.domain.GlobalStats;
import com.moirae.rosettaflow.mapper.domain.StatisticsDataTrend;
import com.moirae.rosettaflow.service.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Resource
    private StatisticsManager statisticsManager;

    @Override
    public List<StatisticsDataTrend> listGlobalDataFileStatsTrendMonthly() {
        return statisticsManager.listGlobalDataFileStatsTrendMonthly();
    }

    @Override
    public GlobalStats globalStats() {
        return statisticsManager.globalStats();
    }
}
