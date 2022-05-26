package com.datum.platform.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datum.platform.manager.PowerServerManager;
import com.datum.platform.mapper.PowerServerMapper;
import com.datum.platform.mapper.domain.PowerServer;
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
