package com.datum.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.datum.platform.mapper.domain.PowerServer;

public interface PowerServerMapper extends BaseMapper<PowerServer> {

    PowerServer statisticsOfGlobal();
}
