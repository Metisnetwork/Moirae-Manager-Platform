package com.moirae.rosettaflow.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.TaskResultConsumer;

import java.util.List;

public interface TaskResultConsumerManager extends IService<TaskResultConsumer> {

    List<TaskResultConsumer> listByTaskId(String taskId);
}
