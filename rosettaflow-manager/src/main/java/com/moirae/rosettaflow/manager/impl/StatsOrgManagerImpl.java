package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.manager.StatsOrgManager;
import com.moirae.rosettaflow.mapper.StatsOrgMapper;
import com.moirae.rosettaflow.mapper.domain.StatsOrg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StatsOrgManagerImpl extends ServiceImpl<StatsOrgMapper, StatsOrg> implements StatsOrgManager {

}
