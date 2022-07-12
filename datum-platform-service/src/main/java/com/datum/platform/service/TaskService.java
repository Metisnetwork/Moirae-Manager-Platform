package com.datum.platform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.datum.platform.common.enums.TaskStatusEnum;
import com.datum.platform.mapper.domain.*;
import com.datum.platform.mapper.enums.MetaDataCertificateTypeEnum;

import java.util.Date;
import java.util.List;

public interface TaskService {

    void batchReplace(List<Task> taskList, List<TaskAlgoProvider> taskAlgoProviderList, List<TaskDataProvider> taskDataProviderList, List<TaskMetaDataColumn> taskMetaDataColumnList, List<TaskPowerProvider> taskPowerProviderList, List<TaskResultConsumer> taskResultConsumerList  );

    IPage<Task> getTaskListByOrg(Long current, Long size, String identityId);

    Task getTaskDetails(String taskId);

    List<TaskEvent> listTaskEventByTaskId(String taskId, String identityId);

    List<Task> getTaskListOfEventNotSynced();

    void syncedEvent(String taskId, List<TaskEvent> taskEventList);

    List<TaskEvent> getTaskEventListFromRemote(String taskId, String identityId);

    IPage<Task> getTaskListByData(Long current, Long size, String metaDataId);

    Task getTaskById(String taskId);

    int countOfTask();

    IPage<Task> getTaskList(Long current, Long size, String keyword, Date begin, Date end, TaskStatusEnum taskStatus);

    Task statisticsOfGlobal();

    List<Task> statisticsOfDay(Integer size);

    List<Task> listTaskOfLatest(Integer size);

    Long countOfMetaDataCertificateUsed(String metaDataId, MetaDataCertificateTypeEnum type, String tokenAddress, String tokenId);

    long getTaskMaxSyncSeq();

    List<Task> listTaskDetail(Long latestSynced, Long size);
}
