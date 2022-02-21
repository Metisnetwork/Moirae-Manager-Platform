package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.manager.TaskResultConsumerManager;
import com.moirae.rosettaflow.mapper.TaskResultConsumerMapper;
import com.moirae.rosettaflow.mapper.domain.TaskPowerProvider;
import com.moirae.rosettaflow.mapper.domain.TaskResultConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TaskResultConsumerManagerImpl extends ServiceImpl<TaskResultConsumerMapper, TaskResultConsumer> implements TaskResultConsumerManager {

    @Override
    public List<TaskResultConsumer> listByTaskId(String taskId) {
        LambdaQueryWrapper<TaskResultConsumer> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(TaskResultConsumer::getTaskId, taskId);
        return list(wrapper);
    }
}
