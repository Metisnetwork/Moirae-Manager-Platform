package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.manager.TaskManager;
import com.moirae.rosettaflow.mapper.TaskMapper;
import com.moirae.rosettaflow.mapper.domain.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TaskManagerImpl extends ServiceImpl<TaskMapper, Task> implements TaskManager {

    @Override
    public IPage<Task> getOrgTaskListByIdentityId(Page<Task> page, String identityId) {
        return this.baseMapper.getOrgTaskListByIdentityId(page, identityId);
    }

    @Override
    public List<Task> getTaskListOfEventNotSynced() {
        return this.baseMapper.getTaskListOfEventNotSynced();
    }

    @Override
    public Task getTaskOfUnSyncedEvent(String taskId) {
        return this.baseMapper.getTaskOfUnSyncedEvent(taskId);
    }

    @Override
    public IPage<Task> getTaskListByMetaDataId(Page<Task> page, String metaDataId) {
        return this.baseMapper.getTaskListByMetaDataId(page, metaDataId);
    }
}
