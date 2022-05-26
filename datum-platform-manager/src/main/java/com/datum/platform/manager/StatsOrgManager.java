package com.datum.platform.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.datum.platform.mapper.domain.StatsOrg;

import java.util.List;

public interface StatsOrgManager extends IService<StatsOrg> {
    List<StatsOrg> listByComputingPowerRatioDesc(Integer size);
}
