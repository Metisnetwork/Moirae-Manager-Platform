package com.moirae.rosettaflow.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.CalculationProcess;
import com.moirae.rosettaflow.mapper.domain.Psi;

import java.util.List;

public interface CalculationProcessManager extends IService<CalculationProcess> {
    List<CalculationProcess> getCalculationProcessList(Long algorithmId);
}
