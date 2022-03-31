package com.moirae.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moirae.rosettaflow.mapper.domain.CalculationProcess;

import java.util.List;

/**
 * @author admin
 */
public interface CalculationProcessMapper extends BaseMapper<CalculationProcess> {

    List<CalculationProcess> getCalculationProcessList(Long algorithmId);
}
