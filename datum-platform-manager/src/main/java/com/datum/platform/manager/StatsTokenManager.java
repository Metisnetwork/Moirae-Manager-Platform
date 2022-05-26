package com.datum.platform.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.datum.platform.mapper.domain.StatsToken;

import java.util.List;

public interface StatsTokenManager extends IService<StatsToken> {

    List<StatsToken> getDataTokenUsedTop(Integer size);
}
