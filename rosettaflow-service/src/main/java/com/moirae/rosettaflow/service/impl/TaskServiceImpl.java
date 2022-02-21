package com.moirae.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moirae.rosettaflow.dto.OrganizationDto;
import com.moirae.rosettaflow.dto.TaskDto;
import com.moirae.rosettaflow.manager.*;
import com.moirae.rosettaflow.mapper.domain.*;
import com.moirae.rosettaflow.service.OrganizationService;
import com.moirae.rosettaflow.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TaskServiceImpl implements TaskService {

    @Resource
    private OrganizationService organizationService;

    @Resource
    private TaskManager taskManager;
    @Resource
    private TaskAlgoProviderManager taskAlgoProviderManager;
    @Resource
    private TaskDataProviderManager taskDataProviderManager;
    @Resource
    private TaskMetaDataColumnManager taskMetaDataColumnManager;
    @Resource
    private TaskPowerProviderManager taskPowerProviderManager;
    @Resource
    private TaskResultConsumerManager taskResultConsumerManager;

    @Override
    @Transactional
    public void batchReplace(List<Task> taskList, List<TaskAlgoProvider> taskAlgoProviderList, List<TaskDataProvider> taskDataProviderList, List<TaskMetaDataColumn> taskMetaDataColumnList, List<TaskPowerProvider> taskPowerProviderList, List<TaskResultConsumer> taskResultConsumerList) {
        taskManager.saveOrUpdateBatch(taskList);
        taskAlgoProviderManager.saveOrUpdateBatch(taskAlgoProviderList);
        Set<String> taskIdSet = taskList.stream().map(Task::getId).collect(Collectors.toSet());

        LambdaQueryWrapper<TaskDataProvider> taskDataProviderWrapper = Wrappers.lambdaQuery();
        taskDataProviderWrapper.in(TaskDataProvider::getTaskId, taskIdSet);
        taskDataProviderManager.remove(taskDataProviderWrapper);
        taskDataProviderManager.saveBatch(taskDataProviderList);

        LambdaQueryWrapper<TaskMetaDataColumn> taskMetaDataColumnWrapper = Wrappers.lambdaQuery();
        taskMetaDataColumnWrapper.in(TaskMetaDataColumn::getTaskId, taskIdSet);
        taskMetaDataColumnManager.remove(taskMetaDataColumnWrapper);
        taskMetaDataColumnManager.saveBatch(taskMetaDataColumnList);

        LambdaQueryWrapper<TaskPowerProvider> taskPowerProviderWrapper = Wrappers.lambdaQuery();
        taskPowerProviderWrapper.in(TaskPowerProvider::getTaskId, taskIdSet);
        taskPowerProviderManager.remove(taskPowerProviderWrapper);
        taskPowerProviderManager.saveBatch(taskPowerProviderList);

        LambdaQueryWrapper<TaskResultConsumer> taskResultConsumerWrapper = Wrappers.lambdaQuery();
        taskResultConsumerWrapper.in(TaskResultConsumer::getTaskId, taskIdSet);
        taskResultConsumerManager.remove(taskResultConsumerWrapper);
        taskResultConsumerManager.saveBatch(taskResultConsumerList);
    }

    @Override
    public IPage<TaskDto> listTaskByIdentityId(Long current, Long size, String identityId) {
        Page<OrganizationDto> page = new Page<>(current, size);
        return taskManager.listTaskByIdentityId(page, identityId);
    }

    @Override
    //TODO
    public TaskDto getTaskDetails(String taskId) {
        // 任务、任务发起方
        Task task = taskManager.getById(taskId);
        // 任务发期方
        task.setSponsor(organizationService.findOrgInfoDetail(task.getOwnerIdentityId()));

        // 任务算法提供方
        TaskAlgoProvider taskAlgoProvider = taskAlgoProviderManager.getById(taskId);
        // 任务数据提供方
        List<TaskDataProvider> taskDataProviderList = taskDataProviderManager.listByTaskId(taskId);
        // 任务算力提供方
        List<TaskPowerProvider> taskPowerProviderList = taskPowerProviderManager.listByTaskId(taskId);
        // 任务结果接收方
        List<TaskResultConsumer> taskResultConsumerList = taskResultConsumerManager.listByTaskId(taskId);

        return null;
    }
}
