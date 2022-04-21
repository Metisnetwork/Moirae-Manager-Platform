package com.moirae.rosettaflow.service;

import com.moirae.rosettaflow.mapper.domain.PowerServer;

import java.util.List;

public interface PowerService {
    void batchReplace(List<PowerServer> powerServerList);

    PowerServer statisticsOfGlobal();
}
