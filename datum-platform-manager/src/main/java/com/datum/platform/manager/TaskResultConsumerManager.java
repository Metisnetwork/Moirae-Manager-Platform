package com.datum.platform.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.datum.platform.mapper.domain.TaskResultConsumer;

import java.util.List;

public interface TaskResultConsumerManager extends IService<TaskResultConsumer> {

    List<TaskResultConsumer> listByTaskId(String taskId);
}
