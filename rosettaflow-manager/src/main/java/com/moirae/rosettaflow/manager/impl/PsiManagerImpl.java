package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.manager.PsiManager;
import com.moirae.rosettaflow.mapper.PsiMapper;
import com.moirae.rosettaflow.mapper.domain.Psi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PsiManagerImpl extends ServiceImpl<PsiMapper, Psi> implements PsiManager {
    @Override
    public List<Psi> listByTrainTaskId(String taskId) {
        LambdaQueryWrapper<Psi> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Psi::getTrainTaskId, taskId);
        return list(wrapper);
    }
}
