package com.moirae.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.common.enums.StatusEnum;
import com.moirae.rosettaflow.mapper.AlgorithmVariableMapper;
import com.moirae.rosettaflow.mapper.domain.AlgorithmVariable;
import com.moirae.rosettaflow.service.IAlgorithmVariableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        LambdaQueryWrapper<AlgorithmVariable> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(AlgorithmVariable::getAlgorithmId, algorithmId);
        queryWrapper.eq(AlgorithmVariable::getStatus, StatusEnum.VALID.getValue());
        return this.list(queryWrapper);
    }
}
