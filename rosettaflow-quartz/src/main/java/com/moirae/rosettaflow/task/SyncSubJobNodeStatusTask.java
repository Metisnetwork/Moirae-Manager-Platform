package com.moirae.rosettaflow.task;

import cn.hutool.core.bean.BeanUtil;
import com.moirae.rosettaflow.common.constants.SysConstant;
import com.moirae.rosettaflow.common.enums.SubJobNodeStatusEnum;
import com.moirae.rosettaflow.common.enums.SubJobStatusEnum;
import com.moirae.rosettaflow.common.enums.TaskRunningStatusEnum;
import com.moirae.rosettaflow.dto.SubJobNodeDto;
import com.moirae.rosettaflow.dto.WorkflowDto;
import com.moirae.rosettaflow.grpc.service.GrpcSysService;
import com.moirae.rosettaflow.grpc.service.GrpcTaskService;
import com.moirae.rosettaflow.grpc.sys.resp.dto.GetTaskResultFileSummaryResponseDto;
import com.moirae.rosettaflow.grpc.task.req.dto.TaskDetailResponseDto;
import com.moirae.rosettaflow.mapper.domain.TaskResult;
import com.moirae.rosettaflow.service.ISubJobNodeService;
import com.moirae.rosettaflow.service.ISubJobService;
import com.moirae.rosettaflow.service.ITaskResultService;
import com.moirae.rosettaflow.service.IWorkflowService;
import com.zengtengpeng.annotation.Lock;
import com.zengtengpeng.operation.RedissonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    private ISubJobNodeService subJobNodeService;

    @Resource
    private ISubJobService subJobService;

    @Resource
    private IWorkflowService workflowService;

    @Resource
    private RedissonObject redissonObject;

    @Resource
    private GrpcSysService grpcSysService;

    @Resource
    private ITaskResultService taskResultService;

    @Scheduled(fixedDelay = 30 * 1000, initialDelay = 60 * 1000)
    @Transactional(rollbackFor = RuntimeException.class)
    @Lock(keys = "SyncSubJobNodeStatusTask")
    public void run() {
        List<SubJobNodeDto> subJobNodeDtoList = subJobNodeService.getRunningNodeWithWorkIdAndNodeNum();
        //如果没有需要同步的数据则不进行同步
        if (subJobNodeDtoList.size() == 0) {
            return;
        }
        log.info("同步更新子作业节点运行中任务开始>>>>");
        Map<String, SubJobNodeDto> subJobNodeMap = subJobNodeDtoList.stream().collect(Collectors.toMap(SubJobNodeDto::getTaskId, subJobNodeDto -> subJobNodeDto));
        //子作业需要更新为成功的列表
        List<Long> subJobSuccessIds = new ArrayList<>();
        //子作业需要更新为失败的列表
        List<Long> subJobFailIds = new ArrayList<>();
        //子作业节点需要更新为成功的列表
        List<Long> subJobNodeSuccessIds = new ArrayList<>();
        //子作业节点需要更新为失败的列表
        List<Long> subJobNodeFailIds = new ArrayList<>();
        //待更新任务结果数据集(当前组织参与的待保存任务结果文件摘要列表)
        List<TaskResult> saveTaskResultList = new ArrayList<>();

        //获取所有任务详情
        List<TaskDetailResponseDto> taskDetailResponseDtoList = grpcTaskService.getTaskDetailList();
        String taskId;
        SubJobNodeDto node;
        for (TaskDetailResponseDto taskDetailResponseDto : taskDetailResponseDtoList) {
            taskId = taskDetailResponseDto.getInformation().getTaskId();
            int state = taskDetailResponseDto.getInformation().getState();
            if (subJobNodeMap.containsKey(taskId)) {
                node = subJobNodeMap.get(taskId);
                if (state == TaskRunningStatusEnum.SUCCESS.getValue()) {
                    //获取待保存任务结果数据
                    GetTaskResultFileSummaryResponseDto taskResultResponseDto = grpcSysService.getTaskResultFileSummary(taskId);
                    if (Objects.isNull(taskResultResponseDto)) {
                        log.error("WorkflowNodeStatusMockTask获取任务结果失败！");
                        continue;
                    }
                    TaskResult taskResult = BeanUtil.copyProperties(taskResultResponseDto, TaskResult.class);
                    saveTaskResultList.add(taskResult);

                    //如果是最后一个节点，需要更新整个子作业状态成功
                    if (node.getNodeStep().equals(node.getNodeNumber())) {
                        subJobSuccessIds.add(node.getSubJobId());
                    } else {
                        //如果有下一个节点，则启动下一个节点
                        log.info("同步更新子作业节点运行中任务,启动下一个节点>>>>redis key:{}", SysConstant.REDIS_SUB_JOB_PREFIX_KEY + taskId);
                        WorkflowDto workflowDto = redissonObject.getValue(SysConstant.REDIS_SUB_JOB_PREFIX_KEY + taskId);
                        log.info("同步更新子作业节点运行中任务,启动下一个节点>>>>redis workflowDto:{}", workflowDto);
                        if (!Objects.isNull(workflowDto)) {
                            //前一个节点taskId
                            workflowDto.setPreTaskId(taskId);
                            workflowDto.setPreTaskResult(taskResult);
                            workflowService.start(workflowDto);
                        }
                    }
                    subJobNodeSuccessIds.add(node.getId());
                    //执行成功，清除redis中的key
                    redissonObject.delete(SysConstant.REDIS_SUB_JOB_PREFIX_KEY + taskId);
                } else if (state == TaskRunningStatusEnum.FAIL.getValue()) {
                    //如果是最后一个节点，需要更新整个子作业状态失败
                    if (node.getNodeStep().equals(node.getNodeNumber())) {
                        subJobFailIds.add(node.getSubJobId());
                    }
                    subJobNodeFailIds.add(node.getId());
                }
            }
        }
        //更新子作业成功记录
        if (subJobSuccessIds.size() > 0) {
            subJobService.updateRunStatus(subJobSuccessIds.toArray(), SubJobStatusEnum.RUN_SUCCESS.getValue());
        }
        //更新子作业失败记录
        if (subJobFailIds.size() > 0) {
            subJobService.updateRunStatus(subJobFailIds.toArray(), SubJobStatusEnum.RUN_FAIL.getValue());
        }
        //更新子作业节点节点成功记录
        if (subJobNodeSuccessIds.size() > 0) {
            subJobNodeService.updateRunStatus(subJobNodeSuccessIds.toArray(), SubJobNodeStatusEnum.RUN_SUCCESS.getValue());
        }
        //更新子作业节点失败记录
        if (subJobNodeFailIds.size() > 0) {
            subJobNodeService.updateRunStatus(subJobNodeFailIds.toArray(), SubJobNodeStatusEnum.RUN_FAIL.getValue());
        }
        //更新任务结果记录
        if (saveTaskResultList.size() > 0) {
            taskResultService.batchInsert(saveTaskResultList);
        }
        log.info("同步更新子作业节点运行中任务结束>>>>");
    }
}
