package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moirae.rosettaflow.common.enums.TaskStatusEnum;
import com.moirae.rosettaflow.mapper.domain.*;

import java.util.Date;
import java.util.List;

public interface TaskService {

    void batchReplace(List<Task> taskList, List<TaskAlgoProvider> taskAlgoProviderList, List<TaskDataProvider> taskDataProviderList, List<TaskMetaDataColumn> taskMetaDataColumnList, List<TaskPowerProvider> taskPowerProviderList, List<TaskResultConsumer> taskResultConsumerList  );

    IPage<Task> getTaskListByOrg(Long current, Long size, String identityId);

    Task getTaskDetails(String taskId);

    List<TaskEvent> getTaskEventList(String taskId);

    List<Task> getTaskListOfEventNotSynced();

    void syncedEvent(String taskId, List<TaskEvent> taskEventList);

    List<TaskEvent> getTaskEventListFromRemote(String taskId, String identityId);

    IPage<Task> getTaskListByData(Long current, Long size, String metaDataId);

    Task getTaskById(String taskId);

    int getTaskStats();

    IPage<Task> getTaskList(Long current, Long size, String keyword, Date begin, Date end, TaskStatusEnum taskStatus);
}
