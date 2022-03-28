package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.manager.AlgorithmVariableManager;
import com.moirae.rosettaflow.mapper.AlgorithmVariableMapper;
import com.moirae.rosettaflow.mapper.domain.AlgorithmVariable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AlgorithmVariableManagerImpl extends ServiceImpl<AlgorithmVariableMapper, AlgorithmVariable> implements AlgorithmVariableManager {
    @Override
    public List<AlgorithmVariable> getByAlgorithmId(Long id) {
        LambdaQueryWrapper<AlgorithmVariable> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(AlgorithmVariable::getAlgorithmId, id);
        return list(wrapper);
    }
}
