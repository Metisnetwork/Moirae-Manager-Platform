package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moirae.rosettaflow.mapper.domain.*;

import java.util.List;

public interface TaskService {

    void batchReplace(List<Task> taskList, List<TaskAlgoProvider> taskAlgoProviderList, List<TaskDataProvider> taskDataProviderList, List<TaskMetaDataColumn> taskMetaDataColumnList, List<TaskPowerProvider> taskPowerProviderList, List<TaskResultConsumer> taskResultConsumerList  );

    IPage<Task> getOrgTaskListByIdentityId(Long current, Long size, String identityId);

    Task getTaskDetails(String taskId);

    List<TaskEvent> getTaskEventList(String taskId);

    List<Task> getTaskListOfEventNotSynced();

    void syncedEvent(String taskId, List<TaskEvent> taskEventList);

    List<TaskEvent> getTaskEventListFromRemote(String taskId, String identityId);

    IPage<Task> getTaskListByMetaDataId(Long current, Long size, String metaDataId);

    Task getTask(String keyword);
}
