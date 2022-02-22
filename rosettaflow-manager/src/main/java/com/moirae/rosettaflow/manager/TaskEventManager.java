package com.moirae.rosettaflow.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.TaskEvent;

import java.util.List;

public interface TaskEventManager extends IService<TaskEvent> {
    List<TaskEvent> listByTaskId(String taskId);
}
