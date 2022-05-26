package com.datum.platform.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datum.platform.manager.StatsTokenManager;
import com.datum.platform.mapper.StatsTokenMapper;
import com.datum.platform.mapper.domain.StatsToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class StatsTokenManagerImpl extends ServiceImpl<StatsTokenMapper, StatsToken> implements StatsTokenManager {

    @Override
    public List<StatsToken> getDataTokenUsedTop(Integer size) {
        return this.baseMapper.getDataTokenUsedTop(size);
    }
}
