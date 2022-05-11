package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.manager.ModelManager;
import com.moirae.rosettaflow.mapper.ModelMapper;
import com.moirae.rosettaflow.mapper.domain.Model;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ModelManagerImpl extends ServiceImpl<ModelMapper, Model> implements ModelManager {
    @Override
    public List<Model> queryAvailableModel(String address, Long algorithmId, String identityId) {
        return baseMapper.queryAvailableModel(address, algorithmId, identityId);
    }

    @Override
    public Model getModelByOrgAndTrainTaskId(String identity, String taskId) {
        LambdaQueryWrapper<Model> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Model::getIdentityId, identity);
        wrapper.eq(Model::getTrainTaskId, taskId);
        return getOne(wrapper);
    }
}
