package com.moirae.rosettaflow.task;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.moirae.rosettaflow.common.constants.SysConstant;
import com.moirae.rosettaflow.common.enums.TaskRunningStatusEnum;
import com.moirae.rosettaflow.common.enums.WorkflowRunStatusEnum;
import com.moirae.rosettaflow.dto.WorkflowDto;
import com.moirae.rosettaflow.grpc.service.GrpcSysService;
import com.moirae.rosettaflow.grpc.service.GrpcTaskService;
import com.moirae.rosettaflow.grpc.sys.resp.dto.GetTaskResultFileSummaryResponseDto;
import com.moirae.rosettaflow.grpc.task.req.dto.TaskDetailResponseDto;
import com.moirae.rosettaflow.mapper.domain.TaskResult;
import com.moirae.rosettaflow.mapper.domain.WorkflowNode;
import com.moirae.rosettaflow.service.ITaskResultService;
import com.moirae.rosettaflow.service.IWorkflowNodeService;
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
import java.util.stream.Collectors;

/**
 * @author hudenian
 * @date 2021/9/29
 * @description 同步更新工作流节点中运行中的任务
 */
@Slf4j
@Component
@Profile({"prod", "test", "local", "xty"})
public class WorkflowNodeStatusTask {
    /**
     * 查询 当前时间之前168小时(7天)的正在运行的数据
     */
    private static final int BEFORE_HOUR = -168;

    @Resource
    private GrpcTaskService grpcTaskService;

    @Resource
    private IWorkflowNodeService workflowNodeService;

    @Resource
    private IWorkflowService workflowService;

    @Resource
    private RedissonObject redissonObject;

    @Resource
    private GrpcSysService grpcSysService;

    @Resource
    private ITaskResultService taskResultService;

    @Scheduled(fixedDelay = 15 * 1000, initialDelay = 15 * 1000)
    @Transactional(rollbackFor = RuntimeException.class)
    @Lock(keys = "WorkflowNodeStatusTask")
    public void run() {
        List<WorkflowNode> workflowNodeList = workflowNodeService.getRunningNode(BEFORE_HOUR);
        //如果没有需要同步的数据则不进行同步
        if (CollUtil.isEmpty(workflowNodeList)) {
            return;
        }
        log.info("同步更新工作流节点中待确认任务开始，一共有{}条数据待更新,分别为：{}>>>>", workflowNodeList.size(), workflowNodeList);
        Map<String, WorkflowNode> workflowNodeMap = workflowNodeList.stream().collect(Collectors.toMap(WorkflowNode::getTaskId, workflowNode -> workflowNode));
        //工作流需要更新为成功的列表
        List<Long> workflowSuccessIds = new ArrayList<>();
        //工作流需要更新为失败的列表
        List<Long> workflowFailIds = new ArrayList<>();
        //工作流节点需要更新为成功的列表
        List<Long> workflowNodeSuccessIds = new ArrayList<>();
        //工作流节点需要更新为失败的列表
        List<Long> workflowNodeFailIds = new ArrayList<>();
        //待更新任务结果数据集(当前组织参与的待保存任务结果文件摘要列表)
        List<TaskResult> saveTaskResultList = new ArrayList<>();

        //获取所的任务详情
        List<TaskDetailResponseDto> taskDetailResponseDtoList = grpcTaskService.getTaskDetailList();

        String taskId;
        WorkflowNode node;
        for (TaskDetailResponseDto taskDetailResponseDto : taskDetailResponseDtoList) {
            taskId = taskDetailResponseDto.getInformation().getTaskId();
            if (workflowNodeMap.containsKey(taskId)) {
                node = workflowNodeMap.get(taskId);
                log.info("工作流id:{},taskId:{},rosettanet处理结束!", node.getWorkflowId(), node.getTaskId());
                if (taskDetailResponseDto.getInformation().getState() == TaskRunningStatusEnum.SUCCESS.getValue()) {
                    log.info("任务id>>>{},处理状态>>>{}", taskDetailResponseDto.getInformation().getTaskId(), taskDetailResponseDto.getInformation().getState());
                    //获取待保存任务结果数据
                    GetTaskResultFileSummaryResponseDto taskResultResponseDto = grpcSysService.getTaskResultFileSummary(taskId);
                    if (taskResultResponseDto == null) {
                        log.error("WorkflowNodeStatusMockTask,taskId:{}获取任务结果失败！", taskId);
                        continue;
                    }
                    TaskResult taskResult = BeanUtil.copyProperties(taskResultResponseDto, TaskResult.class);
                    saveTaskResultList.add(taskResult);

                    //如果是最后一个节点，需要更新整个工作流的状态为成功
                    if (null == node.getNextNodeStep() || node.getNextNodeStep() < 1) {
                        workflowSuccessIds.add(node.getWorkflowId());
                    } else {
                        //如果有下一个节点，则启动下一个节点
                        WorkflowDto workflowDto = redissonObject.getValue(SysConstant.REDIS_WORKFLOW_PREFIX_KEY + taskId);
                        if (null != workflowDto) {
                            //前一个节点taskId
                            workflowDto.setPreTaskId(taskId);
                            workflowDto.setPreTaskResult(taskResult);
                            try {
                                workflowService.start(workflowDto);
                            } catch (Exception e) {
                                log.error("工作流id:{},任务id:{},对应下一个节点任务处理失败原因：{}", node.getWorkflowId(), taskId, e.getMessage());
                                redissonObject.delete(SysConstant.REDIS_WORKFLOW_PREFIX_KEY + taskId);
                            }
                        }
                    }
                    workflowNodeSuccessIds.add(node.getId());
                    //执行成功，清除redis中的key
                    redissonObject.delete(SysConstant.REDIS_WORKFLOW_PREFIX_KEY + taskId);
                } else if (taskDetailResponseDto.getInformation().getState() == TaskRunningStatusEnum.FAIL.getValue()) {
                    log.error("任务id>>>{},处理状态>>>{}", taskDetailResponseDto.getInformation().getTaskId(), taskDetailResponseDto.getInformation().getState());
                    //单个节点失败，需要更新整个工作流的状态为失败
                    workflowFailIds.add(node.getWorkflowId());
                    workflowNodeFailIds.add(node.getId());
                }
            }
        }

        //更新工作流成功记录
        if (workflowSuccessIds.size() > 0) {
            workflowService.updateRunStatus(workflowSuccessIds.toArray(), WorkflowRunStatusEnum.RUN_SUCCESS.getValue());
        }
        //更新工作流失败记录
        if (workflowFailIds.size() > 0) {
            workflowService.updateRunStatus(workflowFailIds.toArray(), WorkflowRunStatusEnum.RUN_FAIL.getValue());
        }
        //更新工作流节点成功记录
        if (workflowNodeSuccessIds.size() > 0) {
            workflowNodeService.updateRunStatus(workflowNodeSuccessIds.toArray(), WorkflowRunStatusEnum.RUN_SUCCESS.getValue());
        }
        //更新工作流节点失败记录
        if (workflowNodeFailIds.size() > 0) {
            workflowNodeService.updateRunStatus(workflowNodeFailIds.toArray(), WorkflowRunStatusEnum.RUN_FAIL.getValue());
        }
        //更新任务结果记录
        if (saveTaskResultList.size() > 0) {
            taskResultService.batchInsert(saveTaskResultList);
        }
        log.info("同步更新工作流节点中待确认任务结束>>>>");
    }
}
