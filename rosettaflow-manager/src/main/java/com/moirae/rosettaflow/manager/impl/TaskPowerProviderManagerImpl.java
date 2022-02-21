package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.manager.TaskPowerProviderManager;
import com.moirae.rosettaflow.mapper.TaskPowerProviderMapper;
import com.moirae.rosettaflow.mapper.domain.TaskPowerProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TaskPowerProviderManagerImpl extends ServiceImpl<TaskPowerProviderMapper, TaskPowerProvider> implements TaskPowerProviderManager {

    @Override
    public List<TaskPowerProvider> listByTaskId(String taskId) {
        LambdaQueryWrapper<TaskPowerProvider> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(TaskPowerProvider::getTaskId, taskId);
        return list(wrapper);
    }
}
