package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.manager.StatsGlobalManager;
import com.moirae.rosettaflow.mapper.StatsGlobalMapper;
import com.moirae.rosettaflow.mapper.domain.StatsGlobal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StatsGlobalManagerImpl extends ServiceImpl<StatsGlobalMapper, StatsGlobal> implements StatsGlobalManager {
}
