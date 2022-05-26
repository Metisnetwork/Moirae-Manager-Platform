package com.datum.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.datum.platform.mapper.domain.CalculationProcess;

import java.util.List;

/**
 * @author admin
 */
public interface CalculationProcessMapper extends BaseMapper<CalculationProcess> {

    List<CalculationProcess> getCalculationProcessList(Long algorithmId);
}
