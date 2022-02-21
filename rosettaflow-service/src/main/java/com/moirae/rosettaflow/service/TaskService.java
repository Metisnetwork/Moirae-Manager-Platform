package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moirae.rosettaflow.dto.TaskDto;
import com.moirae.rosettaflow.mapper.domain.*;

import java.util.List;

public interface TaskService {

    void batchReplace(List<Task> taskList, List<TaskAlgoProvider> taskAlgoProviderList, List<TaskDataProvider> taskDataProviderList, List<TaskMetaDataColumn> taskMetaDataColumnList, List<TaskPowerProvider> taskPowerProviderList, List<TaskResultConsumer> taskResultConsumerList  );

    IPage<TaskDto> listTaskByIdentityId(Long current, Long size, String identityId);

    TaskDto getTaskDetails(String taskId);
}
