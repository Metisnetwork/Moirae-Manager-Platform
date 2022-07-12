package com.datum.platform.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datum.platform.manager.StatsMetaDataManager;
import com.datum.platform.mapper.StatsMetaDataMapper;
import com.datum.platform.mapper.domain.StatsMetaData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class StatsMetaDataManagerImpl extends ServiceImpl<StatsMetaDataMapper, StatsMetaData> implements StatsMetaDataManager {

    @Override
    public List<StatsMetaData> getMetaDataUsedTop(Integer size) {
        return this.baseMapper.getMetaDataUsedTop(size);
    }
}
