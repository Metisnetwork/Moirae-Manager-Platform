package com.moirae.rosettaflow.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.TaskDataProvider;

import java.util.List;

public interface TaskDataProviderManager extends IService<TaskDataProvider> {

    List<TaskDataProvider> listByTaskId(String taskId);

    Long countOfTokenUsed(List<String> metaDataIdList);
}
