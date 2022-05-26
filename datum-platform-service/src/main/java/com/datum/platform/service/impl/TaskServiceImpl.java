package com.datum.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.datum.platform.common.enums.ErrorMsg;
import com.datum.platform.common.enums.RespCodeEnum;
import com.datum.platform.common.enums.TaskStatusEnum;
import com.datum.platform.common.exception.BusinessException;
import com.datum.platform.grpc.client.GrpcTaskServiceClient;
import com.datum.platform.manager.*;
import com.datum.platform.mapper.domain.*;
import com.datum.platform.service.DataService;
import com.datum.platform.service.OrgService;
import com.datum.platform.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TaskServiceImpl implements TaskService {

    @Resource
    private OrgService organizationService;
    @Resource
    private DataService dataService;
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
    @Resource
    private TaskEventManager taskEventManager;
    @Resource
    private TaskExpandManager taskExpandManager;
    @Resource
    private GrpcTaskServiceClient grpcTaskService;

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
    public IPage<Task> getTaskListByOrg(Long current, Long size, String identityId) {
        Page<Task> page = new Page<>(current, size);
        return taskManager.getOrgTaskListByIdentityId(page, identityId);
    }

    @Override
    public Task getTaskDetails(String taskId) {
        Map<String, Org> identityId2OrgMap = organizationService.getIdentityId2OrgMap();
        // 任务
        Task task = taskManager.getById(taskId);
        // 任务发起方
        task.setSponsor(identityId2OrgMap.get(task.getOwnerIdentityId()));
        // 任务算法提供方
        TaskAlgoProvider taskAlgoProvider = taskAlgoProviderManager.getById(taskId);
        taskAlgoProvider.setNodeName(identityId2OrgMap.get(taskAlgoProvider.getIdentityId()).getNodeName());
        task.setAlgoProvider(taskAlgoProvider);
        // 任务数据提供方
        List<TaskDataProvider> taskDataProviderList = taskDataProviderManager.listByTaskId(taskId);
        Map<String, MetaData> metaDataId2MetaDataMap = dataService.getMetaDataId2MetaDataMap(taskDataProviderList.stream().map(TaskDataProvider::getMetaDataId).collect(Collectors.toSet()));
        task.setDataProviderList(taskDataProviderList.stream()
                .map(item -> {
                    item.setNodeName(identityId2OrgMap.get(item.getIdentityId()).getNodeName());
                    item.setMetaDataName(item.getMetaDataName());
                    if(metaDataId2MetaDataMap.containsKey(item.getMetaDataId())){
                        item.setDataTokenName(metaDataId2MetaDataMap.get(item.getMetaDataId()).getTokenName());
                    }else{
                        item.setDataTokenName("");
                    }
                    return item;
                })
                .collect(Collectors.toList())
        );
        // 任务算力提供方
        List<TaskPowerProvider> taskPowerProviderList = taskPowerProviderManager.listByTaskId(taskId);
        taskPowerProviderList.forEach(item -> {
            item.setNodeName(identityId2OrgMap.get(item.getIdentityId()).getNodeName());
        });
        task.setPowerProviderList(taskPowerProviderList);
        // 任务结果接收方
        List<TaskResultConsumer> taskResultConsumerList = taskResultConsumerManager.listByTaskId(taskId);
        taskResultConsumerList.forEach(item -> {
            item.setNodeName(identityId2OrgMap.get(item.getIdentityId()).getNodeName());
        });
        task.setResultReceiverList(taskResultConsumerList);
        // 事件列表
        task.setEventList(listTaskEventByTaskId(taskId, task.getOwnerIdentityId()));
        return task;
    }

    @Override
    public List<TaskEvent> listTaskEventByTaskId(String taskId, String identityId) {
        Task task = taskManager.getTaskOfUnSyncedEvent(taskId);
        List<TaskEvent> result;
        if(task != null){
            result = taskEventManager.listByTaskId(taskId);
        }else{
            result = this.getTaskEventListFromRemote(taskId, identityId);
        }
        // 补充组织名称
        Map<String, Org> identityId2OrgMap = organizationService.getIdentityId2OrgMap();
        result.forEach(item ->{
            if(identityId2OrgMap.containsKey(item.getIdentityId())){
                item.setNodeName(identityId2OrgMap.get(item.getIdentityId()).getNodeName());
            }
        });
        return result;
    }

    @Override
    public List<Task> getTaskListOfEventNotSynced() {
        return taskManager.getTaskListOfEventNotSynced();
    }

    @Override
    @Transactional
    public void syncedEvent(String taskId, List<TaskEvent> taskEventList) {
        TaskExpand taskExpand = new TaskExpand();
        taskExpand.setId(taskId);
        taskExpand.setEventSynced(true);
        taskExpandManager.save(taskExpand);
        taskEventManager.saveBatch(taskEventList);
    }

    @Override
    public List<TaskEvent> getTaskEventListFromRemote(String taskId, String identityId) {
        try {
            List<com.datum.platform.grpc.service.types.TaskEvent> taskEventShowDtoList = grpcTaskService.getTaskEventList(taskId);
            return taskEventShowDtoList.stream()
                    .map(item -> {
                        TaskEvent taskEvent = new TaskEvent();
                        taskEvent.setTaskId(item.getTaskId());
                        taskEvent.setIdentityId(item.getIdentityId());
                        taskEvent.setPartyId(item.getPartyId());
                        taskEvent.setEventType(item.getType());
                        taskEvent.setEventContent(item.getContent());
                        taskEvent.setEventAt(new Date(item.getCreateAt()));
                        return taskEvent;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("调用rpc接口异常--获取运行日志, taskId:{}, 错误信息:{}", taskId, e);
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.RPC_INTERFACE_FAIL.getMsg());
        }
    }

    @Override
    public IPage<Task> getTaskListByData(Long current, Long size, String metaDataId) {
        Page<Task> page = new Page<>(current, size);
        return taskManager.getTaskListByData(page, metaDataId);
    }

    @Override
    public Task getTaskById(String keyword) {
        return taskManager.getById(keyword);
    }

    @Override
    public int countOfTask() {
        return taskManager.count();
    }

    @Override
    public IPage<Task> getTaskList(Long current, Long size, String keyword, Date begin, Date end, TaskStatusEnum taskStatus) {
        Page<Task> page = new Page<>(current, size);
        return taskManager.getTaskList(page, keyword, begin, end, taskStatus.getValue());
    }

    @Override
    public Task statisticsOfGlobal() {
        return taskManager.statisticsOfGlobal();
    }

    @Override
    public List<Task> statisticsOfDay(Integer size) {
        return taskManager.statisticsOfDay(size);
    }

    @Override
    public Long countOfTokenUsed(List<String> metaDataIdList) {
        return taskDataProviderManager.countOfTokenUsed(metaDataIdList);
    }

    @Override
    public List<Task> listTaskOfLatest(Integer size) {
        return taskManager.listTaskOfLatest(size);
    }
}
