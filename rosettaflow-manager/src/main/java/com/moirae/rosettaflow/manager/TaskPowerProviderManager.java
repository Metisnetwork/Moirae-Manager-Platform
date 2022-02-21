package com.moirae.rosettaflow.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.TaskPowerProvider;

import java.util.List;

public interface TaskPowerProviderManager extends IService<TaskPowerProvider> {

    List<TaskPowerProvider> listByTaskId(String taskId);
}
