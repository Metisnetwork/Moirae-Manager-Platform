package com.moirae.rosettaflow.manager.impl;

import com.moirae.rosettaflow.manager.StatisticsManager;
import com.moirae.rosettaflow.mapper.StatisticsMapper;
import com.moirae.rosettaflow.mapper.domain.GlobalStats;
import com.moirae.rosettaflow.mapper.domain.StatisticsDataTrend;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Slf4j
@Service
public class StatisticsManagerImpl implements StatisticsManager {

    @Resource
    private StatisticsMapper statisticsMapper;

    @Override
    public List<StatisticsDataTrend> listGlobalDataFileStatsTrendMonthly() {
        return statisticsMapper.listGlobalDataFileStatsTrendMonthly();
    }

    @Override
    public GlobalStats globalStats() {
        return statisticsMapper.globalStats();
    }
}
