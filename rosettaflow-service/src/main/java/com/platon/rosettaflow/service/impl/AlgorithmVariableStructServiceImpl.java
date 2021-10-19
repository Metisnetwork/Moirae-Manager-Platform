package com.platon.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.common.enums.StatusEnum;
import com.platon.rosettaflow.mapper.AlgorithmVariableStructMapper;
import com.platon.rosettaflow.service.IAlgorithmVariableStructService;
import com.platon.rosettaflow.mapper.domain.AlgorithmVariableStruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author hudenian
 * @date 2021/10/13
 * @description 算法变量模板结构
 */
@Slf4j
@Service
public class AlgorithmVariableStructServiceImpl extends ServiceImpl<AlgorithmVariableStructMapper, AlgorithmVariableStruct> implements IAlgorithmVariableStructService {
    @Override
    public AlgorithmVariableStruct getByAlgorithmId(Long algorithmId) {
        LambdaQueryWrapper<AlgorithmVariableStruct> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(AlgorithmVariableStruct::getAlgorithmId,algorithmId);
        wrapper.eq(AlgorithmVariableStruct::getStatus, StatusEnum.VALID.getValue());
        return this.getOne(wrapper);
    }
}
