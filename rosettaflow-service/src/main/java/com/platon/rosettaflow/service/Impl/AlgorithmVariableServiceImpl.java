package com.platon.rosettaflow.service.Impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.mapper.AlgorithmVariableMapper;
import com.platon.rosettaflow.mapper.domain.AlgorithmVariable;
import com.platon.rosettaflow.service.IAlgorithmVariableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/31
 * @description 功能描述
 */
@Slf4j
@Service
public class AlgorithmVariableServiceImpl extends ServiceImpl<AlgorithmVariableMapper, AlgorithmVariable> implements IAlgorithmVariableService {
    @Override
    public List<AlgorithmVariable> getByAlgorithmId(Long algorithmId) {
        LambdaUpdateWrapper<AlgorithmVariable> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(AlgorithmVariable::getAlgorithmId, algorithmId);
        return this.list(wrapper);
    }
}
