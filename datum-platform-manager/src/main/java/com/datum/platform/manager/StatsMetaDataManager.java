package com.datum.platform.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.datum.platform.mapper.domain.StatsMetaData;

import java.util.List;

public interface StatsMetaDataManager extends IService<StatsMetaData> {

    List<StatsMetaData> getMetaDataUsedTop(Integer size);
}
