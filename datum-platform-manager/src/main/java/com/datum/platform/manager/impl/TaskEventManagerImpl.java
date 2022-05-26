package com.datum.platform.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datum.platform.manager.TaskEventManager;
import com.datum.platform.mapper.TaskEventMapper;
import com.datum.platform.mapper.domain.TaskEvent;
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
