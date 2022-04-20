package com.moirae.rosettaflow.task;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.moirae.rosettaflow.grpc.client.GrpcTaskServiceClient;
import com.moirae.rosettaflow.grpc.dynamic.DataPolicy1;
import com.moirae.rosettaflow.grpc.service.types.TaskDetail;
import com.moirae.rosettaflow.grpc.service.types.TaskDetailSummary;
import com.moirae.rosettaflow.grpc.service.types.TaskOrganization;
import com.moirae.rosettaflow.grpc.service.types.TaskPowerResourceOption;
import com.moirae.rosettaflow.mapper.domain.*;
import com.moirae.rosettaflow.mapper.enums.DataSyncTypeEnum;
import com.moirae.rosettaflow.mapper.enums.TaskStatusEnum;
import com.moirae.rosettaflow.mapper.enums.UserTypeEnum;
import com.moirae.rosettaflow.service.DataSyncService;
import com.moirae.rosettaflow.service.TaskService;
import com.zengtengpeng.annotation.Lock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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

    private void batchUpdateTask(List<TaskDetail> taskDetailResponseDtoList) {
        List<Task> taskList = new ArrayList<>();
        List<TaskAlgoProvider> taskAlgoProviderList = new ArrayList<>();
        List<TaskDataProvider> taskDataProviderList = new ArrayList<>();
        List<TaskMetaDataColumn> taskMetaDataColumnList = new ArrayList<>();
        List<TaskPowerProvider> taskPowerProviderList = new ArrayList<>();
        List<TaskResultConsumer> taskResultConsumerList = new ArrayList<>();
        taskDetailResponseDtoList.stream().forEach(item ->{
            TaskDetailSummary information = item.getInformation();

            TaskAlgoProvider taskAlgoProvider = new TaskAlgoProvider();
            taskAlgoProvider.setTaskId(information.getTaskId());
            taskAlgoProvider.setIdentityId(information.getAlgoSupplier().getIdentityId());
            taskAlgoProvider.setPartyId(information.getAlgoSupplier().getPartyId());
            taskAlgoProviderList.add(taskAlgoProvider);

            Map<String, TaskOrganization> dataMap =  information.getDataSuppliersList().stream().collect(Collectors.toMap(TaskOrganization::getPartyId, org -> org));
            if(information.getDataPolicyType() == 1){
                List<DataPolicy1> dataPolicy1List = JSONArray.parseArray(information.getDataPolicyOption(), DataPolicy1.class);
                dataPolicy1List.stream().forEach(subItem ->{
                    TaskDataProvider taskDataProvider = new TaskDataProvider();
                    taskDataProvider.setTaskId(information.getTaskId());
                    taskDataProvider.setMetaDataId(subItem.getMetadataId());
                    taskDataProvider.setIdentityId(dataMap.get(subItem.getPartyId()).getIdentityId());
                    taskDataProvider.setPartyId(subItem.getPartyId());
                    taskDataProviderList.add(taskDataProvider);
                });
            }

            Map<String, TaskPowerResourceOption> resourceMap = information.getPowerResourceOptionsList().stream().collect(Collectors.toMap(TaskPowerResourceOption::getPartyId, resource -> resource));
            Map<String, TaskOrganization> powerMap =  information.getPowerSuppliersList().stream().collect(Collectors.toMap(TaskOrganization::getPartyId, org -> org));
            if(information.getPowerPolicyType() == 1 || information.getPowerPolicyType() == 2){
                List<String> powerPolicy1List = JSONArray.parseArray(information.getPowerPolicyOption(), String.class);
                powerPolicy1List.forEach(subItem ->{
                    TaskPowerProvider taskPowerProvider = new TaskPowerProvider();
                    taskPowerProvider.setTaskId(information.getTaskId());
                    taskPowerProvider.setIdentityId(information.getPowerPolicyType() == 1 ? powerMap.get(subItem).getIdentityId() : dataMap.get(subItem).getIdentityId());
                    taskPowerProvider.setPartyId(subItem);
                    taskPowerProvider.setUsedCore(resourceMap.get(subItem).getResourceUsedOverview().getUsedProcessor());
                    taskPowerProvider.setUsedMemory(resourceMap.get(subItem).getResourceUsedOverview().getUsedMem());
                    taskPowerProvider.setUsedBandwidth(resourceMap.get(subItem).getResourceUsedOverview().getUsedBandwidth());
                    taskPowerProviderList.add(taskPowerProvider);
                });
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
