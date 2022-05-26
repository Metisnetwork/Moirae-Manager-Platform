package com.datum.platform.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datum.platform.manager.StatsGlobalManager;
import com.datum.platform.mapper.StatsGlobalMapper;
import com.datum.platform.mapper.domain.StatsGlobal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StatsGlobalManagerImpl extends ServiceImpl<StatsGlobalMapper, StatsGlobal> implements StatsGlobalManager {
}
