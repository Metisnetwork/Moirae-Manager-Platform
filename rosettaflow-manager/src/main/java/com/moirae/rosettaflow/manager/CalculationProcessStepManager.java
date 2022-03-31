package com.moirae.rosettaflow.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.CalculationProcessStep;

import java.util.List;

public interface CalculationProcessStepManager extends IService<CalculationProcessStep> {
    List<CalculationProcessStep> getList(Long calculationProcessId);
}
