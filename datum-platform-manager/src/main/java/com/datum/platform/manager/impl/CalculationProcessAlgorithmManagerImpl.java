package com.datum.platform.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datum.platform.manager.CalculationProcessAlgorithmManager;
import com.datum.platform.mapper.CalculationProcessAlgorithmMapper;
import com.datum.platform.mapper.domain.CalculationProcessAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CalculationProcessAlgorithmManagerImpl extends ServiceImpl<CalculationProcessAlgorithmMapper, CalculationProcessAlgorithm> implements CalculationProcessAlgorithmManager {
}
