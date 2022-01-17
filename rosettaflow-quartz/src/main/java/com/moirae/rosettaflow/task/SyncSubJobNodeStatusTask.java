package com.moirae.rosettaflow.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.moirae.rosettaflow.grpc.service.GrpcTaskService;
import com.moirae.rosettaflow.grpc.task.req.dto.TaskDetailResponseDto;
import com.moirae.rosettaflow.mapper.domain.SubJob;
import com.moirae.rosettaflow.mapper.domain.WorkflowRunTaskStatus;
import com.moirae.rosettaflow.service.IWorkflowRunStatusService;
import com.zengtengpeng.annotation.Lock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author juzix
 * @description 同步更新子作业节点中运行中的任务
 */
@Slf4j
@Component
@Profile({"prod", "test", "local", "xty"})
public class SyncSubJobNodeStatusTask {

    @Resource
    private GrpcTaskService grpcTaskService;
    @Resource
    private IWorkflowRunStatusService workflowRunStatusService;

    @Scheduled(fixedDelay = 30 * 1000, initialDelay = 60 * 1000)
    @Transactional(rollbackFor = RuntimeException.class)
    @Lock(keys = "SyncSubJobNodeStatusTask")
    public void run() {
        List<WorkflowRunTaskStatus> workflowRunTaskStatusList = workflowRunStatusService.queryUnConfirmedWorkflowRunTaskStatus();
        //如果没有需要同步的数据则不进行同步
        if (CollUtil.isEmpty(workflowRunTaskStatusList)) {
            return;
        }
        log.info("同步更新子作业节点运行中任务开始>>>>");
        Map<String, Long> workflowRunTaskStatusMap = workflowRunTaskStatusList.stream().collect(Collectors.toMap(WorkflowRunTaskStatus::getTaskId, WorkflowRunTaskStatus::getWorkflowRunId));
        //获取所有任务详情
        List<TaskDetailResponseDto> taskDetailResponseDtoList = grpcTaskService.getTaskDetailList();
        for (TaskDetailResponseDto taskDetailResponseDto : taskDetailResponseDtoList) {
            String taskId = taskDetailResponseDto.getInformation().getTaskId();
            int state = taskDetailResponseDto.getInformation().getState();
            long taskStartAt = taskDetailResponseDto.getInformation().getStartAt();
            long taskEndAt = taskDetailResponseDto.getInformation().getEndAt();
            if (workflowRunTaskStatusMap.containsKey(taskId)) {
                workflowRunStatusService.taskFinish(workflowRunTaskStatusMap.get(taskId), taskId, state, taskStartAt, taskEndAt);
            }
        }
        log.info("同步更新子作业节点运行中任务结束>>>>");
    }


    /**
     * 构造更新子作业对象
     *
     * @param id           子作业id
     * @param taskStartAt  开始时间
     * @param taskEndAt    结束时间
     * @param subJobStatus 作业状态
     * @return 子作业
     */
    private SubJob buildUpdateSubJob(long id, long taskStartAt, long taskEndAt, Byte subJobStatus) {
        SubJob subJob = new SubJob();
        subJob.setId(id);
        subJob.setSubJobStatus(subJobStatus);
        subJob.setBeginTime(taskStartAt > 0 ? new Date(taskStartAt) : null);
        subJob.setEndTime(taskEndAt > 0 ? new Date(taskEndAt) : null);
        subJob.setRunTime((taskStartAt > 0 && taskEndAt > 0) ? String.valueOf(DateUtil.between(subJob.getBeginTime(), subJob.getEndTime(), DateUnit.SECOND)) : null);
        return subJob;
    }
}
