package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.manager.StatsDayManager;
import com.moirae.rosettaflow.mapper.StatsDayMapper;
import com.moirae.rosettaflow.mapper.domain.StatsDay;
import com.moirae.rosettaflow.mapper.enums.StatsDayKeyEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class StatsDayManagerImpl extends ServiceImpl<StatsDayMapper, StatsDay> implements StatsDayManager {
    @Override
    public List<StatsDay> getNewestList(StatsDayKeyEnum key, Integer size) {
        LambdaQueryWrapper<StatsDay> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StatsDay::getStatsKey, key);
        wrapper.orderByDesc(StatsDay::getStatsTime);
        wrapper.last(" limit " + size);
        return list(wrapper);
    }
}
