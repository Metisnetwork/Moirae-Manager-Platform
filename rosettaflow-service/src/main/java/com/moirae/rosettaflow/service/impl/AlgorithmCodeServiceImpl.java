package com.moirae.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.common.enums.StatusEnum;
import com.moirae.rosettaflow.mapper.AlgorithmCodeMapper;
import com.moirae.rosettaflow.mapper.domain.AlgorithmCode;
import com.moirae.rosettaflow.service.IAlgorithmCodeService;
import com.moirae.rosettaflow.service.IWorkflowNodeCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 算法实现类
 * @author admin
 * @date 2021/8/23
 */
@Slf4j
@Service
public class AlgorithmCodeServiceImpl extends ServiceImpl<AlgorithmCodeMapper, AlgorithmCode> implements IAlgorithmCodeService {

    @Resource
    private IWorkflowNodeCodeService workflowNodeCodeService;

    @Override
    public void addAlgorithmCode(AlgorithmCode algorithmCode) {
        this.save(algorithmCode);
    }

    @Override
    public void updateAlgorithmCode(AlgorithmCode algorithmCode) {
        LambdaUpdateWrapper<AlgorithmCode> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(AlgorithmCode::getAlgorithmId, algorithmCode.getAlgorithmId());
        updateWrapper.eq(AlgorithmCode::getStatus, StatusEnum.VALID.getValue());
        this.update(algorithmCode, updateWrapper);

    }

    @Override
    public AlgorithmCode getByAlgorithmId(Long algorithmId) {
        LambdaUpdateWrapper<AlgorithmCode> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(AlgorithmCode::getAlgorithmId, algorithmId);
        wrapper.eq(AlgorithmCode::getStatus, StatusEnum.VALID.getValue());
        return this.getOne(wrapper);
    }
}
