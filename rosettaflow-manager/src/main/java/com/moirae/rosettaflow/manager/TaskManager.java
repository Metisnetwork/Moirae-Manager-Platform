package com.moirae.rosettaflow.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.Task;

import java.util.Date;
import java.util.List;

public interface TaskManager extends IService<Task> {

    IPage<Task> getOrgTaskListByIdentityId(Page<Task> page, String identityId);

    List<Task> getTaskListOfEventNotSynced();

    Task getTaskOfUnSyncedEvent(String taskId);

    IPage<Task> getTaskListByData(Page<Task> page, String metaDataId);

    IPage<Task> getTaskList(Page<Task> page, String keyword, Date begin, Date end, Integer status);

    int countOfTaskAddress();

    Task statisticsOfGlobal();

    List<Task> statisticsOfDay(Date newly);
}
