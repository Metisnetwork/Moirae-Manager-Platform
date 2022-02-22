package com.moirae.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moirae.rosettaflow.mapper.domain.Task;

import java.util.List;

/**
 * t_task_event
 *
 * @author admin
 */
public interface TaskMapper extends BaseMapper<Task> {
    IPage<Task> getOrgTaskListByIdentityId(Page<Task> page, String identityId);

    List<Task> getTaskListOfEventNotSynced();

    Task getTaskOfUnSyncedEvent(String taskId);
}
