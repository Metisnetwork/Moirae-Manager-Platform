package com.datum.platform.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datum.platform.manager.TaskManager;
import com.datum.platform.mapper.TaskMapper;
import com.datum.platform.mapper.domain.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

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
    public List<Task> statisticsOfDay(Integer size) {
        return this.baseMapper.statisticsOfDay(size);
    }

    @Override
    public List<Task> listTaskOfLatest(Integer size) {
        return this.baseMapper.listTaskOfLatest(size);
    }

    @Override
    public long getMaxSyncSeq() {
        LambdaQueryWrapper<Task> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.orderByDesc(Task::getSyncSeq);
        queryWrapper.last("limit 1");
        Task task = getOne(queryWrapper);
        return task == null ? 0L : task.getSyncSeq();
    }

    @Override
    public List<Task> getExistedTaskIdAndUpdateAtList(Set<String> collect) {
        LambdaQueryWrapper<Task> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.select(Task::getId, Task::getUpdateAt, Task::getSyncSeq);
        queryWrapper.in(Task::getId, collect);
        return list(queryWrapper);
    }

    @Override
    public List<Task> listTask(Long latestSynced, Long size) {
        return this.baseMapper.list(latestSynced, size);
    }

}
