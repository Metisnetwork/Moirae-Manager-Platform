package com.datum.platform.service;

import com.datum.platform.mapper.domain.PowerServer;

import java.util.List;

public interface PowerService {
    void batchReplace(List<PowerServer> powerServerList);

    PowerServer statisticsOfGlobal();
}
