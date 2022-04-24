package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.manager.StatsDataManager;
import com.moirae.rosettaflow.mapper.StatsDataMapper;
import com.moirae.rosettaflow.mapper.domain.StatsData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class StatsDataManagerImpl extends ServiceImpl<StatsDataMapper, StatsData> implements StatsDataManager {

    @Override
    public List<StatsData> getDataTokenUsedTop(Integer size) {
        return this.baseMapper.getDataTokenUsedTop(size);
    }
}
