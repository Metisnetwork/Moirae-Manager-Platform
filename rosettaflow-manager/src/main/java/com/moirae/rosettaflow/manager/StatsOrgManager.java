package com.moirae.rosettaflow.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.StatsOrg;

import java.util.List;

public interface StatsOrgManager extends IService<StatsOrg> {
    List<StatsOrg> listByComputingPowerRatioDesc(Integer size);
}
