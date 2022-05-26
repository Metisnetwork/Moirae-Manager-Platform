package com.datum.platform.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.datum.platform.mapper.domain.TaskPowerProvider;

import java.util.List;

public interface TaskPowerProviderManager extends IService<TaskPowerProvider> {

    List<TaskPowerProvider> listByTaskId(String taskId);
}
