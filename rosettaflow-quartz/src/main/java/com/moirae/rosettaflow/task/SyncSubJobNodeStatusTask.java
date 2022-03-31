package com.moirae.rosettaflow.task;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.moirae.rosettaflow.common.enums.DataSyncTypeEnum;
import com.moirae.rosettaflow.grpc.service.GrpcTaskService;
import com.moirae.rosettaflow.grpc.task.req.dto.TaskDetailResponseDto;
import com.moirae.rosettaflow.mapper.domain.ZOldWorkflowRunTaskStatus;
import com.moirae.rosettaflow.service.IDataSyncService;
import com.moirae.rosettaflow.service.ZOldIWorkflowRunStatusService;
import com.moirae.rosettaflow.service.OrgService;
import com.zengtengpeng.annotation.Lock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author juzix
 * @description 同步更新子作业节点中运行中的任务
 */
@Slf4j
@Component
public class SyncSubJobNodeStatusTask {

    @Resource
    private GrpcTaskService grpcTaskService;
    @Resource
    private ZOldIWorkflowRunStatusService workflowRunStatusService;
    @Resource
    private IDataSyncService dataSyncService;
    @Resource
    private OrgService organizationService;

//    @Scheduled(fixedDelay = 5 * 1000, initialDelay = 60 * 1000)
    @Lock(keys = "SyncSubJobNodeStatusTask")
    public void run() {
        List<ZOldWorkflowRunTaskStatus> workflowRunTaskStatusList = workflowRunStatusService.queryUnConfirmedWorkflowRunTaskStatus();
        // 执行取消逻辑
        workflowRunTaskStatusList = workflowRunTaskStatusList.stream()
                .filter(item -> !workflowRunStatusService.cancel(item))
                .collect(Collectors.toList());
        //如果没有需要同步的数据则不进行同步
        if (CollUtil.isEmpty(workflowRunTaskStatusList)) {
            return;
        }
        log.info("同步更新子作业节点运行中任务开始>>>>");
        // 通道 -> List<任务>
        Map<String, List<ZOldWorkflowRunTaskStatus>> channelTaskSetMap = workflowRunTaskStatusList.stream().collect(Collectors.groupingBy(ZOldWorkflowRunTaskStatus::getSenderIdentityId));
        //获取所有任务详情
        for (String identityId: channelTaskSetMap.keySet()) {
            Map<String, Long> workflowRunTaskStatusMap = channelTaskSetMap.get(identityId).stream().collect(Collectors.toMap(ZOldWorkflowRunTaskStatus::getTaskId, ZOldWorkflowRunTaskStatus::getWorkflowRunId));
            try{
                dataSyncService.sync(DataSyncTypeEnum.TASK.getDataType() + "--" + identityId, DataSyncTypeEnum.TASK.getDesc(),//1.根据dataType同步类型获取新的同步时间DataSync
                    (latestSynced) -> {//2.根据新的同步时间latestSynced获取分页列表grpcResponseList
                        return grpcTaskService.getTaskDetailList(organizationService.getChannel(identityId), latestSynced);
                    },
                    (grpcResponseList) -> {//3.根据分页列表grpcResponseList实现实际业务逻辑
                        for (TaskDetailResponseDto taskDetailResponseDto : grpcResponseList) {
                            String taskId = taskDetailResponseDto.getInformation().getTaskId();
                            int state = taskDetailResponseDto.getInformation().getState();
                            long taskStartAt = taskDetailResponseDto.getInformation().getStartAt();
                            long taskEndAt = taskDetailResponseDto.getInformation().getEndAt();
                            if (workflowRunTaskStatusMap.containsKey(taskId)) {
                                try{
                                    log.info("同步更新子作业节点运行中任务开始 taskId = {}, result = {}", taskId, JSONObject.toJSON(taskDetailResponseDto));
                                    workflowRunStatusService.taskFinish(workflowRunTaskStatusMap.get(taskId), taskId, state, taskStartAt, taskEndAt);
                                }catch (Exception e){
                                    log.error("同步更新子作业节点运行中任务异常 taskId = " + taskId, e);
                                }
                            }
                        }
                    },
                    (grpcResponseList) -> {//4.根据分页列表grpcResponseList获取最新的同步时间latestSynced
                        return grpcResponseList
                                .get(grpcResponseList.size() - 1)
                                .getInformation().getUpdateAt();
                    });
            } catch (Exception e){
                log.error("同步更新子作业节点运行中任务失败>>>>", e);
            }
        }
        log.info("同步更新子作业节点运行中任务结束>>>>");
    }
}
