package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.manager.TaskEventManager;
import com.moirae.rosettaflow.mapper.TaskEventMapper;
import com.moirae.rosettaflow.mapper.domain.TaskEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TaskEventManagerImpl extends ServiceImpl<TaskEventMapper, TaskEvent> implements TaskEventManager {

    @Override
    public List<TaskEvent> listByTaskId(String taskId) {
        LambdaQueryWrapper<TaskEvent> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(TaskEvent::getTaskId, taskId);
        return list(wrapper);
    }
}
