package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.manager.CalculationProcessAlgorithmManager;
import com.moirae.rosettaflow.manager.CalculationProcessManager;
import com.moirae.rosettaflow.mapper.CalculationProcessAlgorithmMapper;
import com.moirae.rosettaflow.mapper.CalculationProcessMapper;
import com.moirae.rosettaflow.mapper.domain.CalculationProcess;
import com.moirae.rosettaflow.mapper.domain.CalculationProcessAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CalculationProcessAlgorithmManagerImpl extends ServiceImpl<CalculationProcessAlgorithmMapper, CalculationProcessAlgorithm> implements CalculationProcessAlgorithmManager {
}
