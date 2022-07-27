package com.datum.platform.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datum.platform.manager.StatsOrgManager;
import com.datum.platform.mapper.StatsOrgMapper;
import com.datum.platform.mapper.domain.StatsOrg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class StatsOrgManagerImpl extends ServiceImpl<StatsOrgMapper, StatsOrg> implements StatsOrgManager {

    @Override
    public List<StatsOrg> listByComputingPowerRatioDesc(Integer size) {
        return baseMapper.listByComputingPowerRatioDesc(size);
    }
}
