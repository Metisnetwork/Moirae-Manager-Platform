package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.manager.TaskManager;
import com.moirae.rosettaflow.mapper.TaskMapper;
import com.moirae.rosettaflow.mapper.domain.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class TaskManagerImpl extends ServiceImpl<TaskMapper, Task> implements TaskManager {

    @Override
    public IPage<Task> getOrgTaskListByIdentityId(Page<Task> page, String identityId) {
        return this.baseMapper.getTaskListByOrg(page, identityId);
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
    public IPage<Task> getTaskListByData(Page<Task> page, String metaDataId) {
        return this.baseMapper.getTaskListByData(page, metaDataId);
    }

    @Override
    public IPage<Task> getTaskList(Page<Task> page, String keyword, Date begin, Date end, Integer status) {
        return this.baseMapper.getTaskList(page, keyword, begin, end, status);
    }

    @Override
    public Task statisticsOfGlobal() {
        return this.baseMapper.statisticsOfGlobal();
    }

    @Override
    public List<Task> statisticsOfDay(Date newly) {
        return this.baseMapper.statisticsOfDay(newly);
    }
}
