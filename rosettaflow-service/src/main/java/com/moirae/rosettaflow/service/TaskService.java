package com.moirae.rosettaflow.service;

import com.moirae.rosettaflow.mapper.domain.*;

import java.util.List;

public interface TaskService {

    void batchReplace(List<Task> taskList, List<TaskAlgoProvider> taskAlgoProviderList, List<TaskDataProvider> taskDataProviderList, List<TaskMetaDataColumn> taskMetaDataColumnList, List<TaskPowerProvider> taskPowerProviderList, List<TaskResultConsumer> taskResultConsumerList  );

}
