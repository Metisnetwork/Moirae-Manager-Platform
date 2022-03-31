package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.manager.CalculationProcessManager;
import com.moirae.rosettaflow.mapper.CalculationProcessMapper;
import com.moirae.rosettaflow.mapper.domain.CalculationProcess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CalculationProcessManagerImpl extends ServiceImpl<CalculationProcessMapper, CalculationProcess> implements CalculationProcessManager {
    @Override
    public List<CalculationProcess> getCalculationProcessList(Long algorithmId) {
        return baseMapper.getCalculationProcessList(algorithmId);
    }
}
