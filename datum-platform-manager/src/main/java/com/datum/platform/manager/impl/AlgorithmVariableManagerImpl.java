package com.datum.platform.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datum.platform.manager.AlgorithmVariableManager;
import com.datum.platform.mapper.AlgorithmVariableMapper;
import com.datum.platform.mapper.domain.AlgorithmVariable;
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
