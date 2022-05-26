package com.datum.platform.service.impl;

import com.datum.platform.manager.PowerServerManager;
import com.datum.platform.mapper.domain.PowerServer;
import com.datum.platform.service.PowerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class PowerServiceImpl implements PowerService {

    @Resource
    private PowerServerManager powerServerManager;

    @Override
    @Transactional
    public void batchReplace(List<PowerServer> powerServerList) {
        powerServerManager.saveOrUpdateBatch(powerServerList);
    }

    @Override
    public PowerServer statisticsOfGlobal() {
        return powerServerManager.statisticsOfGlobal();
    }
}
