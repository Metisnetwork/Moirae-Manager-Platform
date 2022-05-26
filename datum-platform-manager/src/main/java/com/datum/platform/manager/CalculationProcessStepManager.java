package com.datum.platform.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.datum.platform.mapper.domain.CalculationProcessStep;

import java.util.List;

public interface CalculationProcessStepManager extends IService<CalculationProcessStep> {
    List<CalculationProcessStep> getList(Long calculationProcessId);

    Boolean isEnd(Long calculationProcessId, Integer step);
}
