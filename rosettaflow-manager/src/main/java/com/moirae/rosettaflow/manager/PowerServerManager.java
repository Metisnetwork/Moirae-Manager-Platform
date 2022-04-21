package com.moirae.rosettaflow.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.PowerServer;

public interface PowerServerManager extends IService<PowerServer> {

    PowerServer statisticsOfGlobal();
}
