package com.datum.platform.task;

import carrier.types.Taskdata;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.datum.platform.grpc.client.GrpcTaskServiceClient;
import com.datum.platform.grpc.dynamic.TaskDataPolicyCsv;
import com.datum.platform.grpc.dynamic.TaskDataPolicyPreTask;
import com.datum.platform.grpc.dynamic.TaskPowerPolicy;
import com.datum.platform.grpc.enums.TaskDataPolicyTypesEnum;
import com.datum.platform.grpc.enums.TaskPowerPolicyTypesEnum;
import com.datum.platform.mapper.domain.*;
import com.datum.platform.mapper.enums.DataSyncTypeEnum;
import com.datum.platform.mapper.enums.TaskStatusEnum;
import com.datum.platform.mapper.enums.UserTypeEnum;
import com.datum.platform.service.DataSyncService;
import com.datum.platform.service.TaskService;
import com.zengtengpeng.annotation.Lock;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 同步元数据定时任务, 多久同步一次待确认
 *
 * @author hudenian
 * @date 2021/8/23
 */
@ConditionalOnProperty(name="dev.quartz", havingValue="true")
@Slf4j
@Component
public class SyncDcTaskTask {

    @Resource
    private GrpcTaskServiceClient grpcTaskService;
    @Resource
    private DataSyncService dataSyncService;
    @Resource
    private TaskService taskService;

    @Scheduled(fixedDelay = 5 * 1000)
    @Lock(keys = "SyncDcTaskTask")
    public void run() {
        long begin = DateUtil.current();
        try {
            dataSyncService.sync(DataSyncTypeEnum.TASK.getDataType(),DataSyncTypeEnum.TASK.getDesc(),//1.根据dataType同步类型获取新的同步时间DataSync
                    (latestSynced) -> {//2.根据新的同步时间latestSynced获取分页列表grpcResponseList
                        return grpcTaskService.getGlobalTaskDetailList(latestSynced);
                    },
                    (grpcResponseList) -> {//3.根据分页列表grpcResponseList实现实际业务逻辑
                        // 批量更新
                        this.batchUpdateTask(grpcResponseList);
                    },
                    (grpcResponseList) -> {//4.根据分页列表grpcResponseList获取最新的同步时间latestSynced
                        return grpcResponseList
                                .get(grpcResponseList.size() - 1)
                                .getInformation().getUpdateAt();
                    });
        } catch (Exception e) {
            log.error("任务信息同步,从net同步任务失败,失败原因：{}", e.getMessage(), e);
        }
        log.info("任务信息同步结束，总耗时:{}ms", DateUtil.current() - begin);
    }

    private void batchUpdateTask(List<Taskdata.TaskDetail> taskDetailResponseDtoList) {
        List<Task> taskList = new ArrayList<>();
        List<TaskAlgoProvider> taskAlgoProviderList = new ArrayList<>();
        List<TaskDataProvider> taskDataProviderList = new ArrayList<>();
        List<TaskMetaDataColumn> taskMetaDataColumnList = new ArrayList<>();
        List<TaskPowerProvider> taskPowerProviderList = new ArrayList<>();
        List<TaskResultConsumer> taskResultConsumerList = new ArrayList<>();
        taskDetailResponseDtoList.stream().forEach(item ->{
            Taskdata.TaskDetailSummary information = item.getInformation();

            TaskAlgoProvider taskAlgoProvider = new TaskAlgoProvider();
            taskAlgoProvider.setTaskId(information.getTaskId());
            taskAlgoProvider.setIdentityId(information.getAlgoSupplier().getIdentityId());
            taskAlgoProvider.setPartyId(information.getAlgoSupplier().getPartyId());
            taskAlgoProviderList.add(taskAlgoProvider);

            Map<String, Taskdata.TaskOrganization> dataMap =  information.getDataSuppliersList().stream().collect(Collectors.toMap(Taskdata.TaskOrganization::getPartyId, org -> org));
            for (int i = 0; i < information.getDataPolicyTypesList().size(); i++) {
                TaskDataProvider taskDataProvider = new TaskDataProvider();
                if(information.getDataPolicyTypesList().get(i) == TaskDataPolicyTypesEnum.POLICY_TYPES_30001.getValue()){
                    TaskDataPolicyPreTask dataPolicy = JSONObject.parseObject(information.getDataPolicyOptions(i), TaskDataPolicyPreTask.class);
                    taskDataProvider.setTaskId(information.getTaskId());
                    taskDataProvider.setMetaDataId("preTask:" + UUID.randomUUID());
                    taskDataProvider.setPolicyType(information.getDataPolicyTypesList().get(i));
                    taskDataProvider.setInputType(dataPolicy.getInputType());
                    taskDataProvider.setIdentityId(dataMap.get(dataPolicy.getPartyId()).getIdentityId());
                    taskDataProvider.setPartyId(dataPolicy.getPartyId());
                }else{
                    TaskDataPolicyCsv dataPolicy = JSONObject.parseObject(information.getDataPolicyOptions(i), TaskDataPolicyCsv.class);
                    taskDataProvider.setTaskId(information.getTaskId());
                    taskDataProvider.setMetaDataId(dataPolicy.getMetadataId());
                    taskDataProvider.setMetaDataName(dataPolicy.getMetadataName());
                    taskDataProvider.setPolicyType(information.getDataPolicyTypesList().get(i));
                    taskDataProvider.setInputType(dataPolicy.getInputType());
                    taskDataProvider.setIdentityId(dataMap.get(dataPolicy.getPartyId()).getIdentityId());
                    taskDataProvider.setPartyId(dataPolicy.getPartyId());
                    taskDataProvider.setKeyColumnIdx(dataPolicy.getKeyColumn() == null ? null : dataPolicy.getKeyColumn().intValue());
                    if(dataPolicy.getSelectedColumns() != null && dataPolicy.getSelectedColumns().size() > 0){
                        taskDataProvider.setSelectedColumns(StringUtils.join(dataPolicy.getSelectedColumns(), ","));
                    }
                    // 设置数据消耗
                    dataPolicy.getConsume().ifPresent(consume ->{
                        taskDataProvider.setConsumeType(consume.getConsumeType());
                        taskDataProvider.setConsumeTokenAddress(consume.getTokenAddress());
                        taskDataProvider.setConsumeTokenId(consume.getTokenId());
                        taskDataProvider.setConsumeBalance(consume.getBalance());
                    });
                }
                taskDataProviderList.add(taskDataProvider);
            }

            Map<String, Taskdata.TaskPowerResourceOption> resourceMap = information.getPowerResourceOptionsList().stream().collect(Collectors.toMap(Taskdata.TaskPowerResourceOption::getPartyId, resource -> resource));
            Map<String, Taskdata.TaskOrganization> powerMap =  information.getPowerSuppliersList().stream().collect(Collectors.toMap(Taskdata.TaskOrganization::getPartyId, org -> org));

            for (int i = 0; i < information.getPowerPolicyTypesList().size(); i++) {
                Integer type = information.getPowerPolicyTypes(i);
                String partyId;
                String providerPartyId = null;
                if(type == TaskPowerPolicyTypesEnum.POLICY_TYPES_1.getValue()){
                    partyId = information.getPowerPolicyOptions(i);
                }else{
                    // type = 2 或 3
                    TaskPowerPolicy taskPowerPolicy = JSONObject.parseObject(information.getPowerPolicyOptions(i), TaskPowerPolicy.class);
                    partyId = taskPowerPolicy.getPowerPartyId();
                    providerPartyId = taskPowerPolicy.getProviderPartyId();
                }
                if(powerMap.containsKey(partyId)){
                    TaskPowerProvider taskPowerProvider = new TaskPowerProvider();
                    taskPowerProvider.setTaskId(information.getTaskId());
                    taskPowerProvider.setIdentityId(powerMap.get(partyId).getIdentityId());
                    taskPowerProvider.setPartyId(partyId);
                    taskPowerProvider.setProviderPartyId(providerPartyId);
                    taskPowerProvider.setPolicyType(type);
                    taskPowerProvider.setUsedCore(resourceMap.get(partyId).getResourceUsedOverview().getUsedProcessor());
                    taskPowerProvider.setUsedMemory(resourceMap.get(partyId).getResourceUsedOverview().getUsedMem());
                    taskPowerProvider.setUsedBandwidth(resourceMap.get(partyId).getResourceUsedOverview().getUsedBandwidth());
                    taskPowerProviderList.add(taskPowerProvider);
                }
            }

            information.getReceiversList().forEach(subItem ->{
                TaskResultConsumer taskResultConsumer = new TaskResultConsumer();
                taskResultConsumer.setTaskId(information.getTaskId());
                taskResultConsumer.setIdentityId(subItem.getIdentityId());
                taskResultConsumer.setConsumerPartyId(subItem.getPartyId());
                taskResultConsumerList.add(taskResultConsumer);
            });
            Task task = new Task();
            task.setId(information.getTaskId());
            task.setTaskName(information.getTaskName());
            task.setAddress(information.getUser());
            task.setUserType(UserTypeEnum.find(information.getUserTypeValue()));
            task.setRequiredMemory(information.getOperationCost().getMemory());
            task.setRequiredCore(information.getOperationCost().getProcessor());
            task.setRequiredBandwidth(information.getOperationCost().getBandwidth());
            task.setRequiredDuration(information.getOperationCost().getDuration());
            task.setOwnerIdentityId(information.getSender().getIdentityId());
            task.setOwnerPartyId(information.getSender().getPartyId());
            task.setCreateAt(new Date(information.getCreateAt()));
            task.setStartAt(new Date(information.getStartAt()));
            task.setEndAt(information.getEndAt() == 0 ? null : new Date(information.getEndAt()));
            task.setStatus(TaskStatusEnum.find(information.getStateValue()));
            taskList.add(task);
        });
        taskService.batchReplace(taskList, taskAlgoProviderList, taskDataProviderList, taskMetaDataColumnList, taskPowerProviderList, taskResultConsumerList);
    }
}
