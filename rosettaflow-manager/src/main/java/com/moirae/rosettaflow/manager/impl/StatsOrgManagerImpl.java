package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.manager.StatsOrgManager;
import com.moirae.rosettaflow.mapper.StatsOrgMapper;
import com.moirae.rosettaflow.mapper.domain.StatsOrg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class StatsOrgManagerImpl extends ServiceImpl<StatsOrgMapper, StatsOrg> implements StatsOrgManager {

    @Override
    public List<StatsOrg> listByComputingPowerRatioDesc(Integer size) {
        LambdaQueryWrapper<StatsOrg> wrapper = Wrappers.lambdaQuery();
        wrapper.orderByDesc(StatsOrg::getComputingPowerRatio);
        wrapper.last("limit " + size );
        return list(wrapper);
    }
}
