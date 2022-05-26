package com.datum.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.datum.platform.mapper.domain.StatsOrg;

import java.util.List;

/**
 * @author admin
 */
public interface StatsOrgMapper extends BaseMapper<StatsOrg> {
    List<StatsOrg> listByComputingPowerRatioDesc(Integer size);
}
