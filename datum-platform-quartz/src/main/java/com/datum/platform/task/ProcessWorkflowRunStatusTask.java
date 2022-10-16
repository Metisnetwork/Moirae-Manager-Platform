package com.datum.platform.task;

import carrier.api.SysRpcApi;
import carrier.api.WorkflowRpcApi;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.datum.platform.common.exception.BusinessException;
import com.datum.platform.grpc.client.GrpcSysServiceClient;
import com.datum.platform.grpc.client.GrpcWorkflowServiceClient;
import com.datum.platform.grpc.constant.GrpcConstant;
import com.datum.platform.manager.WorkflowRunStatusManager;
import com.datum.platform.manager.WorkflowRunStatusTaskManager;
import com.datum.platform.manager.WorkflowRunTaskResultManager;
import com.datum.platform.mapper.domain.*;
import com.datum.platform.mapper.enums.WorkflowTaskRunStatusEnum;
import com.datum.platform.service.*;
import com.zengtengpeng.annotation.Lock;
import common.constant.CarrierEnum;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author juzix
 * @description 同步更新子作业节点中运行中的任务
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "dev.quartz", havingValue = "true")
public class ProcessWorkflowRunStatusTask {

    @Resource
    private WorkflowService workflowService;
    @Resource
    private TaskService taskService;
    @Resource
    private GrpcWorkflowServiceClient grpcWorkflowServiceClient;
    @Resource
    private WorkflowRunStatusManager workflowRunStatusManager;
    @Resource
    private WorkflowRunStatusTaskManager workflowRunStatusTaskManager;
    @Resource
    private OrgService orgService;
    @Resource
    private AlgService algService;
    @Resource
    private GrpcSysServiceClient grpcSysServiceClient;
    @Resource
    private DataService dataService;
    @Resource
    private WorkflowRunTaskResultManager workflowRunTaskResultManager;

    @Scheduled(fixedDelay = 5 * 1000)
    @Lock(keys = "ProcessWorkflowRunStatusTask")
    public void run() {
        log.info("同步更新子作业节点运行中任务开始>>>>");

        //1.获取执行中的工作流
        List<WorkflowRunStatus> runningWorkflowList = workflowRunStatusManager.getRunningWorkflow();
        //如果没有需要同步的数据则不进行同步
        if (CollUtil.isEmpty(runningWorkflowList)) {
            return;
        }

        runningWorkflowList.forEach(workflowRunStatus -> {
            List<WorkflowTask> workflowTasks = workflowService.listExecutableDetailsByWorkflowVersion(
                    workflowRunStatus.getWorkflowId(),
                    workflowRunStatus.getWorkflowVersion());
            Map<Long, WorkflowTask> workflowTaskMap = workflowTasks.stream()
                    .collect(Collectors.toMap(WorkflowTask::getWorkflowTaskId, me -> me));
            //2.获取发起方组织
            WorkflowTask workflowTask = workflowTasks.get(0);
            String identityId = workflowTask.getIdentityId();
            Channel channel = orgService.getChannel(identityId);

            //3.构建请求体
            WorkflowRpcApi.QueryWorkStatusRequest request = WorkflowRpcApi.QueryWorkStatusRequest.newBuilder()
                    .setWorkflowId(0, workflowRunStatus.getWorkflowHash())
                    .build();
            //4.获取工作流信息
            WorkflowRpcApi.QueryWorkStatusResponse response = grpcWorkflowServiceClient.queryWorkFlowStatus(channel, request);
            if (response.getStatus() == GrpcConstant.GRPC_SUCCESS_CODE) {
                //5.响应结果可能包含多个工作流
                List<WorkflowRpcApi.WorkFlowStatus> workflowStatusListList = response.getWorkflowStatusListList();
                workflowStatusListList.forEach(workFlowStatus -> {
                    //更新工作流中的任务状态
                    updateWorkflowRunTaskStatus(workflowTaskMap, workFlowStatus);

                    //更新工作流执行状态
                    updateWorkflowRunStatus(workflowRunStatus, workFlowStatus);
                });
            }
        });

    }

    private void updateWorkflowRunTaskStatus(Map<Long, WorkflowTask> workflowTaskMap,
                                             WorkflowRpcApi.WorkFlowStatus workFlowStatus) {
        List<WorkflowRpcApi.WorkFlowTaskStatus> taskListList = workFlowStatus.getTaskListList();
        taskListList.forEach(task -> {
            String taskId = task.getTaskId();
            if (StrUtil.isBlank(taskId)) {
                return;
            }
            //说明任务还没更新过来
            Task taskById = taskService.getTaskById(taskId);
            if (taskById == null) {
                return;
            }

            WorkflowRunStatusTask byTaskName = workflowRunStatusTaskManager.getByTaskName(task.getTaskName());
            //补全工作流任务模板
            byTaskName.setWorkflowTask(workflowTaskMap.get(byTaskName.getWorkflowTaskId()));
            //如果已经更新过了，则不再更新
            if (byTaskName.getRunStatus() == WorkflowTaskRunStatusEnum.RUN_FAIL
                    || byTaskName.getRunStatus() == WorkflowTaskRunStatusEnum.RUN_SUCCESS) {
                return;
            }
            //设置任务ID
            byTaskName.setTaskId(taskId);
            CarrierEnum.TaskState taskStatus = task.getStatus();
            switch (taskStatus) {
                case TaskState_Pending:
                case TaskState_Running:
                    taskRunning(byTaskName, taskById);
                    break;
                case TaskState_Unknown:
                case TaskState_Failed:
                    taskFail(byTaskName, taskById);
                    break;
                case TaskState_Succeed:
                    taskSuccess(byTaskName, taskById);
                    break;
            }
            //更新工作流中任务的状态
            workflowRunStatusTaskManager.updateById(byTaskName);
        });

    }

    private void updateWorkflowRunStatus(WorkflowRunStatus workflowRunStatus, WorkflowRpcApi.WorkFlowStatus workFlowStatus) {
        CarrierEnum.WorkFlowState status = workFlowStatus.getStatus();
        switch (status) {
            case WorkFlowState_Unknown:
            case WorkFlowState_Failed:
                workflowRunStatus.setRunStatus(WorkflowTaskRunStatusEnum.RUN_FAIL);
                workflowRunStatus.setEndTime(new Date());
                break;
            case WorkFlowState_Succeed:
                workflowRunStatus.setRunStatus(WorkflowTaskRunStatusEnum.RUN_SUCCESS);
                workflowRunStatus.setEndTime(new Date());
                break;
            default:
                break;
        }
        //更新工作流状态
        workflowRunStatusManager.updateById(workflowRunStatus);
    }

    private void taskRunning(WorkflowRunStatusTask curWorkflowRunTaskStatus, Task task) {
        // 更新状态
        curWorkflowRunTaskStatus.setBeginTime(task.getStartAt());
        curWorkflowRunTaskStatus.setRunStatus(WorkflowTaskRunStatusEnum.RUN_DOING);
    }

    private void taskFail(WorkflowRunStatusTask curWorkflowRunTaskStatus, Task task) {
        // 更新状态
        curWorkflowRunTaskStatus.setRunStatus(WorkflowTaskRunStatusEnum.RUN_FAIL);
        curWorkflowRunTaskStatus.setRunMsg("task fail!");
        curWorkflowRunTaskStatus.setBeginTime(task.getStartAt());
        curWorkflowRunTaskStatus.setEndTime(task.getEndAt());
    }

    private void taskSuccess(WorkflowRunStatusTask curWorkflowRunTaskStatus, Task task) {
        WorkflowTask workflowTask = curWorkflowRunTaskStatus.getWorkflowTask();

        // 更新状态
        curWorkflowRunTaskStatus.setRunStatus(WorkflowTaskRunStatusEnum.RUN_SUCCESS);
        curWorkflowRunTaskStatus.setBeginTime(task.getStartAt());
        curWorkflowRunTaskStatus.setEndTime(task.getEndAt());

        // 结果文件
        Set<String> identityIdSet = workflowTask.getOutputList().stream().map(WorkflowTaskOutput::getIdentityId).collect(Collectors.toSet());
        Algorithm algorithm = algService.getAlgorithm(workflowTask.getAlgorithmId(), false);

        List<WorkflowRunTaskResult> taskResultList = new ArrayList<>();
        List<Model> modelList = new ArrayList<>();
        List<Psi> psiList = new ArrayList<>();
        for (String identityId : identityIdSet) {
            ManagedChannel managedChannel = null;
            try {
                managedChannel = orgService.getChannel(identityId);
            } catch (BusinessException businessException) {
                log.error("任务结果输出不存在！ identityId = {}", identityId);
                continue;
            }

            SysRpcApi.GetTaskResultFileSummary response = grpcSysServiceClient.getTaskResultFileSummary(managedChannel, task.getId());
            if (Objects.isNull(response)) {
                log.error("获取任务结果失败！ info = {}", response);
                return;
            }
            // 处理结果
            WorkflowRunTaskResult taskResult = new WorkflowRunTaskResult();
            taskResult.setIdentityId(identityId);
            taskResult.setTaskId(task.getId());
            taskResult.setFileName(response.getMetadataName());
            taskResult.setMetadataId(response.getMetadataId());
            taskResult.setOriginId(response.getOriginId());
            taskResult.setDataType(response.getDataTypeValue());
            taskResult.setMetadataOption(response.getMetadataOption());
            taskResult.setIp(response.getIp());
            taskResult.setPort(response.getPort());
            taskResult.setExtra(response.getExtra());
            taskResultList.add(taskResult);

            JSONObject meta = JSONObject.parseObject(response.getMetadataOption());
            String filePath = "";
            if (meta.containsKey("dataPath")) {
                filePath = meta.getString("dataPath");
            }
            if (meta.containsKey("dirPath")) {
                filePath = meta.getString("dirPath");
            }

            // 处理模型
            if (algorithm.getOutputModel()) {
                Algorithm predictionAlgorithm = algService.getAlgorithmOfRelativelyPrediction(workflowTask.getAlgorithmId());
                Model model = new Model();
                model.setMetaDataId(taskResult.getMetadataId());
                model.setIdentityId(identityId);
                model.setName(algorithm.getAlgorithmName() + "(" + task.getId() + ")");
                model.setFileId(taskResult.getOriginId());
                model.setDataType(response.getDataTypeValue());
                model.setMetadataOption(response.getMetadataOption());
                model.setTrainTaskId(task.getId());
                model.setTrainAlgorithmId(workflowTask.getAlgorithmId());
                model.setTrainUserAddress(task.getAddress());
                model.setSupportedAlgorithmId(predictionAlgorithm.getAlgorithmId());
                model.setFilePath(filePath);
                modelList.add(model);
            }

            if (algorithm.getOutputPsi()) {
                Psi psi = new Psi();
                psi.setMetaDataId(taskResult.getMetadataId());
                psi.setIdentityId(identityId);
                psi.setName(algorithm.getAlgorithmName() + "(" + task.getId() + ")");
                psi.setFileId(taskResult.getOriginId());
                psi.setDataType(response.getDataTypeValue());
                psi.setMetadataOption(response.getMetadataOption());
                psi.setTrainTaskId(task.getId());
                psi.setTrainAlgorithmId(workflowTask.getAlgorithmId());
                psi.setTrainUserAddress(task.getAddress());
                psi.setFilePath(filePath);
                psiList.add(psi);
            }
        }
        if (psiList.size() > 0) {
            dataService.saveBatchPsi(psiList);
        }

        if (modelList.size() > 0) {
            dataService.saveBatchModel(modelList);
        }

        if (taskResultList.size() > 0) {
            workflowRunTaskResultManager.saveBatch(taskResultList);
        }
    }
}
