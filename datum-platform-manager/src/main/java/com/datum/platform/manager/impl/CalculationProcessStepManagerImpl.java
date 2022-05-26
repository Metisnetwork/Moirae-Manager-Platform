package com.datum.platform.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datum.platform.manager.CalculationProcessStepManager;
import com.datum.platform.mapper.CalculationProcessStepMapper;
import com.datum.platform.mapper.domain.CalculationProcessStep;
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

    @Override
    public Boolean isEnd(Long calculationProcessId, Integer step) {
        LambdaQueryWrapper<CalculationProcessStep> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(CalculationProcessStep::getCalculationProcessId, calculationProcessId);
        wrapper.orderByDesc(CalculationProcessStep::getStep);
        wrapper.last(" limit 1");
        return getOne(wrapper).getStep() == step;
    }
}
