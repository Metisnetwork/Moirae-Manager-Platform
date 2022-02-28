package com.moirae.rosettaflow.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.Task;

import java.util.List;

public interface TaskManager extends IService<Task> {

    IPage<Task> getOrgTaskListByIdentityId(Page<Task> page, String identityId);

    List<Task> getTaskListOfEventNotSynced();

    Task getTaskOfUnSyncedEvent(String taskId);

    IPage<Task> getTaskListByMetaDataId(Page<Task> page, String metaDataId);
}
