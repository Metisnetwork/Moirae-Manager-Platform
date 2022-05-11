package com.moirae.rosettaflow.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.StatsDay;
import com.moirae.rosettaflow.mapper.enums.StatsDayKeyEnum;

import java.util.List;

public interface StatsDayManager extends IService<StatsDay> {

    List<StatsDay> getNewestList(StatsDayKeyEnum key, Integer size);
}
