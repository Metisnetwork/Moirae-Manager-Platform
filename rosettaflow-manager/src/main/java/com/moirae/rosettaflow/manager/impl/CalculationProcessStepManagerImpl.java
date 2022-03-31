package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.manager.CalculationProcessStepManager;
import com.moirae.rosettaflow.mapper.CalculationProcessStepMapper;
import com.moirae.rosettaflow.mapper.domain.CalculationProcessStep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CalculationProcessStepManagerImpl extends ServiceImpl<CalculationProcessStepMapper, CalculationProcessStep> implements CalculationProcessStepManager {
    @Override
    public List<CalculationProcessStep> getList(Long calculationProcessId) {
        LambdaQueryWrapper<CalculationProcessStep> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(CalculationProcessStep::getCalculationProcessId, calculationProcessId);
        return list(wrapper);
    }
}
