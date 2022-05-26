package com.datum.platform.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.datum.platform.mapper.domain.PowerServer;

public interface PowerServerManager extends IService<PowerServer> {

    PowerServer statisticsOfGlobal();
}
