package com.moirae.rosettaflow.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.StatsToken;

import java.util.List;

public interface StatsTokenManager extends IService<StatsToken> {

    List<StatsToken> getDataTokenUsedTop(Integer size);
}
