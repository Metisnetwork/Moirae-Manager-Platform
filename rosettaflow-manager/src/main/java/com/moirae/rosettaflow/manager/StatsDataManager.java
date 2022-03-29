package com.moirae.rosettaflow.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.StatsData;

import java.util.List;

public interface StatsDataManager extends IService<StatsData> {

    List<StatsData> getDataTokenUsedTop(Integer size);
}
