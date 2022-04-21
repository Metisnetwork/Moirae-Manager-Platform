package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.manager.PowerServerManager;
import com.moirae.rosettaflow.mapper.PowerServerMapper;
import com.moirae.rosettaflow.mapper.domain.PowerServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PowerServerManagerImpl extends ServiceImpl<PowerServerMapper, PowerServer> implements PowerServerManager {

    @Override
    public PowerServer statisticsOfGlobal() {
        return this.baseMapper.statisticsOfGlobal();
    }
}
