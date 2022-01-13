//package com.moirae.rosettaflow.task;
//
//import cn.hutool.core.bean.BeanUtil;
//import cn.hutool.core.collection.CollUtil;
//import cn.hutool.core.date.DateUnit;
//import cn.hutool.core.date.DateUtil;
//import com.moirae.rosettaflow.common.constants.SysConstant;
//import com.moirae.rosettaflow.common.enums.SubJobNodeStatusEnum;
//import com.moirae.rosettaflow.common.enums.SubJobStatusEnum;
//import com.moirae.rosettaflow.common.enums.TaskRunningStatusEnum;
//import com.moirae.rosettaflow.dto.SubJobNodeDto;
//import com.moirae.rosettaflow.dto.WorkflowDto;
//import com.moirae.rosettaflow.grpc.service.GrpcSysService;
//import com.moirae.rosettaflow.grpc.service.GrpcTaskService;
//import com.moirae.rosettaflow.grpc.sys.resp.dto.GetTaskResultFileSummaryResponseDto;
//import com.moirae.rosettaflow.grpc.task.req.dto.TaskDetailResponseDto;
//import com.moirae.rosettaflow.mapper.domain.SubJob;
//import com.moirae.rosettaflow.mapper.domain.TaskResult;
//import com.moirae.rosettaflow.service.*;
//import com.zengtengpeng.annotation.Lock;
//import com.zengtengpeng.operation.RedissonObject;
//import io.grpc.ManagedChannel;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Profile;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.annotation.Resource;
//import java.util.*;
//import java.util.stream.Collectors;
//
///**
// * @author juzix
// * @description 同步更新子作业节点中运行中的任务
// */
//@Slf4j
//@Component
//@Profile({"prod", "test", "local", "xty"})
//public class SyncSubJobNodeStatusTask {
//
//    @Resource
//    private GrpcTaskService grpcTaskService;
//
//    @Resource
//    private IWorkflowService workflowService;
//
//    @Resource
//    private RedissonObject redissonObject;
//
//    @Resource
//    private GrpcSysService grpcSysService;
//
//    @Resource
//    private IWorkflowNodeOutputService workflowNodeOutputService;
//
//    @Resource
//    private NetManager netManager;
//
//    @Scheduled(fixedDelay = 30 * 1000, initialDelay = 60 * 1000)
//    @Transactional(rollbackFor = RuntimeException.class)
//    @Lock(keys = "SyncSubJobNodeStatusTask")
//    public void run() {
//        List<SubJobNodeDto> subJobNodeDtoList = subJobNodeService.getRunningNodeWithWorkIdAndNodeNum();
//        //如果没有需要同步的数据则不进行同步
//        if (CollUtil.isEmpty(subJobNodeDtoList)) {
//            return;
//        }
//        log.info("同步更新子作业节点运行中任务开始>>>>");
//        Map<String, SubJobNodeDto> subJobNodeMap = subJobNodeDtoList.stream().collect(Collectors.toMap(SubJobNodeDto::getTaskId, subJobNodeDto -> subJobNodeDto));
//        //子作业需要更新为成功的列表
//        List<SubJob> subJobSuccessList = new ArrayList<>();
//        //子作业需要更新为失败的列表
//        List<SubJob> subJobFailList = new ArrayList<>();
//        //子作业节点需要更新为成功的列表
//        List<Long> subJobNodeSuccessIds = new ArrayList<>();
//        //子作业节点需要更新为失败的列表
//        List<Long> subJobNodeFailIds = new ArrayList<>();
//        //待更新任务结果数据集(当前组织参与的待保存任务结果文件摘要列表)
//        List<TaskResult> saveTaskResultList = new ArrayList<>();
//
//        //获取所有任务详情
//        List<TaskDetailResponseDto> taskDetailResponseDtoList = grpcTaskService.getTaskDetailList();
//
//        String taskId;
//        SubJobNodeDto node;
//        for (TaskDetailResponseDto taskDetailResponseDto : taskDetailResponseDtoList) {
//            taskId = taskDetailResponseDto.getInformation().getTaskId();
//            int state = taskDetailResponseDto.getInformation().getState();
//            long taskStartAt = taskDetailResponseDto.getInformation().getStartAt();
//            long taskEndAt = taskDetailResponseDto.getInformation().getEndAt();
//            if (subJobNodeMap.containsKey(taskId)) {
//                node = subJobNodeMap.get(taskId);
//                if (state == TaskRunningStatusEnum.SUCCESS.getValue()) {
//                    //获取待保存任务结果数据
//                    String identityId = workflowNodeOutputService.getOutputIdentityIdByWorkFlowIdAndStep(subJobNodeMap.get(taskId).getWorkflowId(),subJobNodeMap.get(taskId).getNodeStep().longValue());
//                    ManagedChannel channel = netManager.getChannel(identityId);
//                    GetTaskResultFileSummaryResponseDto taskResultResponseDto = grpcSysService.getTaskResultFileSummary(channel, taskId);
//                    if (Objects.isNull(taskResultResponseDto)) {
//                        log.error("WorkflowNodeStatusMockTask获取任务结果失败！");
//                        continue;
//                    }
//                    TaskResult taskResult = BeanUtil.copyProperties(taskResultResponseDto, TaskResult.class);
//                    saveTaskResultList.add(taskResult);
//
//                    //如果是最后一个节点，需要更新整个子作业状态成功
//                    if (node.getNodeStep().equals(node.getNodeNumber())) {
//                        SubJob subJobSuccess = buildUpdateSubJob(node.getSubJobId(), taskStartAt, taskEndAt, SubJobStatusEnum.RUN_SUCCESS.getValue());
//                        subJobSuccessList.add(subJobSuccess);
//                    } else {
//                        //如果有下一个节点，则启动下一个节点
//                        log.info("同步更新子作业节点运行中任务,启动下一个节点>>>>redis key:{}", SysConstant.REDIS_SUB_JOB_PREFIX_KEY + taskId);
//                        WorkflowDto workflowDto = redissonObject.getValue(SysConstant.REDIS_SUB_JOB_PREFIX_KEY + taskId);
//                        log.info("同步更新子作业节点运行中任务,启动下一个节点>>>>redis workflowDto:{}", workflowDto);
//                        if (!Objects.isNull(workflowDto)) {
//                            //前一个节点taskId
//                            workflowDto.setPreTaskId(taskId);
//                            workflowDto.setPreTaskResult(taskResult);
//                            workflowService.start(workflowDto);
//                        }
//                    }
//                    subJobNodeSuccessIds.add(node.getId());
//                    //执行成功，清除redis中的key
//                    redissonObject.delete(SysConstant.REDIS_SUB_JOB_PREFIX_KEY + taskId);
//                } else if (state == TaskRunningStatusEnum.FAIL.getValue()) {
//                    //如果是最后一个节点，需要更新整个子作业状态失败
//                    if (node.getNodeStep().equals(node.getNodeNumber())) {
//                        SubJob subJobFail = buildUpdateSubJob(node.getSubJobId(), taskStartAt, taskEndAt, SubJobStatusEnum.RUN_FAIL.getValue());
//                        subJobFailList.add(subJobFail);
//                    }
//                    subJobNodeFailIds.add(node.getId());
//                }
//            }
//        }
//        //更新子作业成功记录
//        if (subJobSuccessList.size() > 0) {
//            subJobService.updateBatchRunStatus(subJobSuccessList);
//        }
//        //更新子作业失败记录
//        if (subJobFailList.size() > 0) {
//            subJobService.updateBatchRunStatus(subJobFailList);
//        }
//        //更新子作业节点节点成功记录
//        if (subJobNodeSuccessIds.size() > 0) {
//            subJobNodeService.updateBatchRunStatus(subJobNodeSuccessIds.toArray(), SubJobNodeStatusEnum.RUN_SUCCESS.getValue(), SubJobNodeStatusEnum.RUN_SUCCESS.getMsg());
//        }
//        //更新子作业节点失败记录
//        if (subJobNodeFailIds.size() > 0) {
//            subJobNodeService.updateBatchRunStatus(subJobNodeFailIds.toArray(), SubJobNodeStatusEnum.RUN_FAIL.getValue(), SubJobNodeStatusEnum.RUN_FAIL.getMsg());
//        }
//        //更新任务结果记录
//        if (saveTaskResultList.size() > 0) {
//            taskResultService.batchInsert(saveTaskResultList);
//        }
//        log.info("同步更新子作业节点运行中任务结束>>>>");
//    }
//
//
//    /**
//     * 构造更新子作业对象
//     *
//     * @param id           子作业id
//     * @param taskStartAt  开始时间
//     * @param taskEndAt    结束时间
//     * @param subJobStatus 作业状态
//     * @return 子作业
//     */
//    private SubJob buildUpdateSubJob(long id, long taskStartAt, long taskEndAt, Byte subJobStatus) {
//        SubJob subJob = new SubJob();
//        subJob.setId(id);
//        subJob.setSubJobStatus(subJobStatus);
//        subJob.setBeginTime(taskStartAt > 0 ? new Date(taskStartAt) : null);
//        subJob.setEndTime(taskEndAt > 0 ? new Date(taskEndAt) : null);
//        subJob.setRunTime((taskStartAt > 0 && taskEndAt > 0) ? String.valueOf(DateUtil.between(subJob.getBeginTime(), subJob.getEndTime(), DateUnit.SECOND)) : null);
//        return subJob;
//    }
//}
