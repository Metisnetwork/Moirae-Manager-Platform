package com.platon.rosettaflow.service.Impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.mapper.AlgorithmCodeMapper;
import com.platon.rosettaflow.mapper.domain.AlgorithmCode;
import com.platon.rosettaflow.service.IAlgorithmCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author admin
 * @date 2021/8/23
 * @description 算法实现类
 */
@Slf4j
@Service
public class AlgorithmCodeServiceImpl extends ServiceImpl<AlgorithmCodeMapper, AlgorithmCode> implements IAlgorithmCodeService {

    @Override
    public void addAlgorithmCode(AlgorithmCode algorithmCode) {
        this.save(algorithmCode);
    }

    @Override
    public void updateAlgorithmCode(AlgorithmCode algorithmCode) {
        LambdaUpdateWrapper<AlgorithmCode> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(AlgorithmCode::getAlgorithmId, algorithmCode.getAlgorithmId());
        this.update(updateWrapper);

    }

    @Override
    public AlgorithmCode getByAlgorithmId(Long algorithmId) {
        LambdaUpdateWrapper<AlgorithmCode> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(AlgorithmCode::getAlgorithmId, algorithmId);
        return this.getOne(wrapper);
    }

}
