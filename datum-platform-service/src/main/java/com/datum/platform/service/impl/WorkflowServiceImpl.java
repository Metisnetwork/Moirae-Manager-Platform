package com.datum.platform.service.impl;

import carrier.api.SysRpcApi;
import carrier.api.TaskRpcApi;
import carrier.types.Common;
import carrier.types.Resourcedata;
import carrier.types.Taskdata;
import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.datum.platform.common.constants.SysConfig;
import com.datum.platform.common.enums.*;
import com.datum.platform.common.exception.BusinessException;
import com.datum.platform.common.utils.LanguageContext;
import com.datum.platform.grpc.client.GrpcSysServiceClient;
import com.datum.platform.grpc.client.GrpcTaskServiceClient;
import com.datum.platform.grpc.constant.GrpcConstant;
import com.datum.platform.grpc.dynamic.*;
import com.datum.platform.grpc.enums.TaskDataFlowPolicyTypesEnum;
import com.datum.platform.grpc.enums.TaskDataPolicyTypesEnum;
import com.datum.platform.grpc.enums.TaskPowerPolicyTypesEnum;
import com.datum.platform.grpc.enums.TaskReceiverPolicyTypesEnum;
import com.datum.platform.manager.*;
import com.datum.platform.mapper.domain.*;
import com.datum.platform.mapper.enums.TaskStatusEnum;
import com.datum.platform.mapper.enums.*;
import com.datum.platform.service.*;
import com.datum.platform.service.dto.alg.AlgDto;
import com.datum.platform.service.dto.alg.AlgVariableV2Dto;
import com.datum.platform.service.dto.data.HaveAttributesCredentialDto;
import com.datum.platform.service.dto.data.NoAttributesCredentialDto;
import com.datum.platform.service.dto.data.UserWLatCredentialDto;
import com.datum.platform.service.dto.model.ModelDto;
import com.datum.platform.service.dto.org.OrgNameDto;
import com.datum.platform.service.dto.task.TaskEventDto;
import com.datum.platform.service.dto.task.TaskResultDto;
import com.datum.platform.service.dto.token.TokenDto;
import com.datum.platform.service.dto.token.TokenHolderDto;
import com.datum.platform.service.dto.workflow.*;
import com.datum.platform.service.dto.workflow.common.CodeDto;
import com.datum.platform.service.dto.workflow.common.DataInputDto;
import com.datum.platform.service.dto.workflow.common.OutputDto;
import com.datum.platform.service.dto.workflow.common.ResourceDto;
import com.datum.platform.service.dto.workflow.expert.*;
import com.datum.platform.service.dto.workflow.wizard.*;
import com.datum.platform.service.utils.CommonUtils;
import com.datum.platform.service.utils.TreeUtils;
import com.datum.platform.service.utils.UserContext;
import com.google.protobuf.ByteString;
import common.constant.CarrierEnum;
import common.constant.TokenEnum;
import io.grpc.ManagedChannel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WorkflowServiceImpl implements WorkflowService {
    @Resource
    private SysConfig sysConfig;
    @Resource
    private GrpcTaskServiceClient grpcTaskServiceClient;
    @Resource
    private GrpcSysServiceClient grpcSysServiceClient;
    @Resource
    private AlgService algService;
    @Resource
    private DataService dataService;
    @Resource
    private TaskService taskService;
    @Resource
    private OrgService orgService;
    @Resource
    private WorkflowManager workflowManager;
    @Resource
    private WorkflowVersionManager workflowVersionManager;
    @Resource
    private WorkflowTaskManager workflowTaskManager;
    @Resource
    private WorkflowTaskInputManager workflowTaskInputManager;
    @Resource
    private WorkflowTaskOutputManager workflowTaskOutputManager;
    @Resource
    private WorkflowTaskResourceManager workflowTaskResourceManager;
    @Resource
    private WorkflowTaskVariableManager workflowTaskVariableManager;
    @Resource
    private WorkflowSettingWizardManager workflowSettingWizardManager;
    @Resource
    private WorkflowSettingExpertManager workflowSettingExpertManager;
    @Resource
    private CalculationProcessManager calculationProcessManager;
    @Resource
    private CalculationProcessStepManager calculationProcessStepManager;
    @Resource
    private CalculationProcessTaskManager calculationProcessTaskManager;
    @Resource
    private WorkflowRunStatusManager workflowRunStatusManager;
    @Resource
    private WorkflowRunStatusTaskManager workflowRunTaskStatusManager;
    @Resource
    private WorkflowRunTaskResultManager workflowRunTaskResultManager;
    @Resource
    private WorkflowRunStatusCertificateManager workflowRunStatusCertificateManager;

    @Override
    public int getWorkflowCount() {
        return workflowManager.getWorkflowCount(UserContext.getCurrentUser().getAddress());
    }

    @Override
    public IPage<Workflow> getWorkflowList(Long current, Long size, String keyword, Long algorithmId, Date begin, Date end, Integer createMode) {
        Page<Workflow> page = new Page<>(current, size);
        workflowManager.getWorkflowList(page, UserContext.getCurrentUser().getAddress(), keyword, algorithmId, begin, end, createMode);

        page.getRecords().forEach(item -> {
            if(item.getCalculationProcessStepType() != null && item.getCalculationProcessStep() != null){
                CalculationProcessStep calculationProcessStep = new CalculationProcessStep();
                calculationProcessStep.setCalculationProcessId(item.getCalculationProcessId());
                calculationProcessStep.setStep(item.getCalculationProcessStep());
                calculationProcessStep.setType(CalculationProcessTypeEnum.find(item.getCalculationProcessStepType()));
                item.setCalculationProcessStepObject(calculationProcessStep);
            }
        });
        return page;
    }

    @Override
    public IPage<WorkflowVersion> getWorkflowVersionList(Long current, Long size, Long workflowId) {
        Page<WorkflowVersion> page = new Page<>(current, size);
        IPage<WorkflowVersion> result = workflowVersionManager.getWorkflowVersionList(page, workflowId);
        result.getRecords().stream().forEach(item -> {
            if(item.getStatus() == null){
                item.setStatus(WorkflowTaskRunStatusEnum.RUN_NEED);
            }
        });
        return result;
    }

    @Override
    public List<CalculationProcess> getCalculationProcessList(Long algorithmId) {
        List<CalculationProcess> calculationProcessList = calculationProcessManager.getCalculationProcessList(algorithmId);
        calculationProcessList.stream().forEach(item -> {
            item.setStepItem(calculationProcessStepManager.getList(item.getCalculationProcessId()));
        });
        return calculationProcessList;
    }

    @Override
    @Transactional
    public WorkflowVersionKeyDto createWorkflowOfWizardMode(String workflowName, String workflowDesc, Long algorithmId, Long calculationProcessId) {
        checkWorkFlowName(workflowName);

        WorkflowVersionKeyDto result = new WorkflowVersionKeyDto();
        // 配置查询
        AlgorithmClassify rootTree = algService.getAlgorithmClassifyTree(true);
        AlgorithmClassify selectedTree = TreeUtils.findSubTree(rootTree, algorithmId);
        CalculationProcess calculationProcess = getCalculationProcessDetails(calculationProcessId);

        // 创建工作流记录
        Workflow workflow = workflowManager.createOfWizardMode(workflowName, workflowDesc,
                algorithmId, selectedTree.getName(),
                calculationProcessId, LanguageContext.getByLanguage(calculationProcess.getName(), calculationProcess.getNameEn()),
                UserContext.getCurrentUser().getAddress());

        // 创建工作流版本
        workflowVersionManager.create(workflow.getWorkflowId(), workflow.getWorkflowVersion(), StringUtils.join(workflowName, "-v1"));

        // 创建工作流任务配置 1-训练  2-预测  3-训练，并预测 4-PSI
        for (int i = 0; i < calculationProcess.getTaskItem().size(); i++) {
            CalculationProcessTask calculationProcessTask = calculationProcess.getTaskItem().get(i);
            Algorithm algorithm = algService.findAlgorithm(calculationProcessTask.getAlgorithmSelect(), rootTree, selectedTree);
            WorkflowTask workflowTask = workflowTaskManager.createOfWizardMode(
                    workflow.getWorkflowId(), workflow.getWorkflowVersion(),
                    calculationProcessTask.getStep(),
                    algorithm.getAlgorithmId(),
                    algorithm.getInputModel(), calculationProcessTask.getInputModelStep(),
                    algorithm.getSupportDefaultPsi(), calculationProcessTask.getInputPsiStep(),
                    algorithm.getType() == AlgorithmTypeEnum.PT ? Optional.of(WorkflowTaskPowerTypeEnum.RANDOM) : Optional.empty());
            if(algorithm.getAlgorithmVariableList().size() > 0){
                workflowTaskVariableManager.create(workflowTask.getWorkflowTaskId(), algorithm.getAlgorithmVariableList());
            }
            workflowTaskResourceManager.create(workflowTask.getWorkflowTaskId(), algorithm);
        }

        // 创建流程定义
        List<WorkflowSettingWizard> workflowSettingWizardList = calculationProcess.getStepItem().stream()
                .map(item -> {
                    WorkflowSettingWizard wizard = new WorkflowSettingWizard();
                    wizard.setWorkflowId(workflow.getWorkflowId());
                    wizard.setWorkflowVersion(workflow.getWorkflowVersion());
                    wizard.setCalculationProcessStepType(item.getType());
                    wizard.setStep(item.getStep());
                    wizard.setTask1Step(item.getTask1Step());
                    wizard.setTask2Step(item.getTask2Step());
                    wizard.setTask3Step(item.getTask3Step());
                    wizard.setTask4Step(item.getTask4Step());
                    return wizard;
                })
                .collect(Collectors.toList());
        workflowSettingWizardManager.saveBatch(workflowSettingWizardList);
        result.setWorkflowId(workflow.getWorkflowId());
        result.setWorkflowVersion(workflow.getWorkflowVersion());
        return result;
    }

    @Override
    @Transactional
    public WorkflowVersionKeyDto createWorkflowOfExpertMode(String workflowName) {
        checkWorkFlowName(workflowName);

        WorkflowVersionKeyDto result = new WorkflowVersionKeyDto();
        // 创建工作流记录
        Workflow workflow = workflowManager.createOfExpertMode(workflowName, UserContext.getCurrentUser().getAddress());

        // 创建工作流版本
        workflowVersionManager.create(workflow.getWorkflowId(), workflow.getWorkflowVersion(), StringUtils.join(workflowName, "-v1"));
        result.setWorkflowId(workflow.getWorkflowId());
        result.setWorkflowVersion(workflow.getWorkflowVersion());
        return result;
    }

    @Override
    public List<WorkflowRunStatusTask> listWorkflowRunTaskStatusOfUnConfirmed() {
        return  workflowRunTaskStatusManager.listOfUnConfirmed();
    }

    @Override
    @Transactional
    public boolean cancelWorkflowRunTaskStatus(WorkflowRunStatusTask workflowRunTaskStatus) {
        WorkflowRunStatus workflowRunStatus = workflowRunStatusManager.getById(workflowRunTaskStatus.getWorkflowRunId());
        if(workflowRunStatus.getCancelStatus() != null && workflowRunStatus.getCancelStatus() == WorkflowTaskRunStatusEnum.RUN_NEED){
            TaskRpcApi.TerminateTaskRequest request = TaskRpcApi.TerminateTaskRequest.newBuilder()
                    .setUser(workflowRunStatus.getAddress())
                    .setUserType(CarrierEnum.UserType.User_1)
                    .setTaskId(workflowRunTaskStatus.getTaskId())
                    .setSign(ByteString.copyFromUtf8(workflowRunStatus.getSign()))
                    .build();

            try {
                Common.SimpleResponse response = grpcTaskServiceClient.terminateTask(orgService.getChannel(workflowTaskManager.getById(workflowRunTaskStatus.getWorkflowTaskId()).getIdentityId()), request);
                log.info("终止工作流返回， response = {}", response);
                if (response != null && response.getStatus() == GrpcConstant.GRPC_SUCCESS_CODE) {
                    workflowRunStatus.setCancelStatus(WorkflowTaskRunStatusEnum.RUN_SUCCESS);
                    workflowRunStatusManager.updateById(workflowRunStatus);
                }else{
                    log.error("终止工作流失败，失败原因! 原因 = {}", response);
                    workflowRunStatus.setCancelStatus(WorkflowTaskRunStatusEnum.RUN_FAIL);
                    workflowRunStatusManager.updateById(workflowRunStatus);
                }
            } catch (Exception e) {
                log.error("终止工作流失败，失败原因!", e);
                workflowRunStatus.setCancelStatus(WorkflowTaskRunStatusEnum.RUN_FAIL);
                workflowRunStatusManager.updateById(workflowRunStatus);
            }
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void taskFinish(WorkflowRunStatusTask workflowRunTaskStatus, Task task) {
        if (task.getStatus() == TaskStatusEnum.SUCCEED || task.getStatus() == TaskStatusEnum.FAILED) {
            // 生成运行时任务清单明细
            WorkflowRunStatus workflowRunStatus = workflowRunStatusManager.getById(workflowRunTaskStatus.getWorkflowRunId());
            List<WorkflowTask> workflowTaskList = listExecutableDetailsByWorkflowVersion(workflowRunStatus.getWorkflowId(), workflowRunStatus.getWorkflowVersion());
            loadWorkflowRunStatus(workflowRunStatus, workflowTaskList);
            // 如果任务成功
            if(task.getStatus() == TaskStatusEnum.SUCCEED){
                taskSuccess(workflowRunStatus,task);
                if(workflowRunStatus.getCurStep().compareTo(workflowRunStatus.getStep()) < 0) {
                    workflowRunStatus.setCurStep(workflowRunStatus.getWorkflowRunTaskStatusList().stream()
                            .map(WorkflowRunStatusTask::getStep)
                            .filter(item -> item > workflowRunStatus.getCurStep())
                            .min(Integer::compareTo)
                            .get());
                    executeTask(workflowRunStatus);
                }
            }
            // 如果任务成功
            if(task.getStatus() == TaskStatusEnum.FAILED){
                taskFail(workflowRunStatus,task);
            }
        }
    }


    private void taskFail(WorkflowRunStatus workflowRunStatus, Task task) {
        WorkflowRunStatusTask curWorkflowRunTaskStatus = workflowRunStatus.getWorkflowRunTaskStatusList().stream().collect(Collectors.toMap(WorkflowRunStatusTask::getStep, item -> item)).get(workflowRunStatus.getCurStep());

        if(!task.getId().equals(curWorkflowRunTaskStatus.getTaskId())){
            log.error("工作流状态错误！ workflowRunStatusId = {}  task = {}", workflowRunStatus.getId(), task.getId());
        }
        // 更新状态
        curWorkflowRunTaskStatus.setRunStatus(WorkflowTaskRunStatusEnum.RUN_FAIL);
        curWorkflowRunTaskStatus.setRunMsg("task fail!");
        curWorkflowRunTaskStatus.setBeginTime(task.getStartAt());
        curWorkflowRunTaskStatus.setEndTime(task.getEndAt());
        workflowRunTaskStatusManager.updateById(curWorkflowRunTaskStatus);

        workflowRunStatus.setRunStatus(WorkflowTaskRunStatusEnum.RUN_FAIL);
        workflowRunStatus.setEndTime(new Date());
        workflowRunStatusManager.updateById(workflowRunStatus);
    }

    private void taskSuccess(WorkflowRunStatus workflowRunStatus, Task task) {
        WorkflowRunStatusTask curWorkflowRunTaskStatus = workflowRunStatus.getWorkflowRunTaskStatusList().stream().collect(Collectors.toMap(WorkflowRunStatusTask::getStep, item -> item)).get(workflowRunStatus.getCurStep());
        WorkflowTask workflowTask = curWorkflowRunTaskStatus.getWorkflowTask();

        if(!task.getId().equals(curWorkflowRunTaskStatus.getTaskId())){
            log.error("工作流状态错误！ workflowRunStatusId = {}  task = {}", workflowRunStatus.getId(), task.getId());
            return;
        }
        // 更新状态
        curWorkflowRunTaskStatus.setRunStatus(WorkflowTaskRunStatusEnum.RUN_SUCCESS);
        curWorkflowRunTaskStatus.setBeginTime(task.getStartAt());
        curWorkflowRunTaskStatus.setEndTime(task.getEndAt());
        workflowRunTaskStatusManager.updateById(curWorkflowRunTaskStatus);

        // 最后一个任务
        if(workflowRunStatus.getCurStep().compareTo(workflowRunStatus.getStep()) == 0){
            workflowRunStatus.setRunStatus(WorkflowTaskRunStatusEnum.RUN_SUCCESS);
            workflowRunStatus.setEndTime(new Date());
            workflowRunStatusManager.updateById(workflowRunStatus);
        }

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
            } catch (BusinessException businessException){
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
            taskResultList.add(taskResult);

            JSONObject meta = JSONObject.parseObject(response.getMetadataOption());
            String filePath = "";
            if(meta.containsKey("dataPath")){
                filePath = meta.getString("dataPath");
            }
            if(meta.containsKey("dirPath")){
                filePath = meta.getString("dirPath");
            }

            // 处理模型
            if(algorithm.getOutputModel()){
                Algorithm predictionAlgorithm = algService.getAlgorithmOfRelativelyPrediction(workflowTask.getAlgorithmId());
                Model model = new Model();
                model.setMetaDataId(taskResult.getMetadataId());
                model.setIdentityId(identityId);
                model.setName(algorithm.getAlgorithmName()+"(" + task.getId() + ")");
                model.setFileId(taskResult.getOriginId());
                model.setDataType(response.getDataTypeValue());
                model.setMetadataOption(response.getMetadataOption());
                model.setTrainTaskId(task.getId());
                model.setTrainAlgorithmId(workflowTask.getAlgorithmId());
                model.setTrainUserAddress(workflowRunStatus.getAddress());
                model.setSupportedAlgorithmId(predictionAlgorithm.getAlgorithmId());
                model.setEvaluate(response.getExtra());
                model.setFilePath(filePath);
                modelList.add(model);
            }

            if(algorithm.getOutputPsi()){
                Psi psi = new Psi();
                psi.setMetaDataId(taskResult.getMetadataId());
                psi.setIdentityId(identityId);
                psi.setName(algorithm.getAlgorithmName()+"(" + task.getId() + ")");
                psi.setFileId(taskResult.getOriginId());
                psi.setDataType(response.getDataTypeValue());
                psi.setMetadataOption(response.getMetadataOption());
                psi.setTrainTaskId(task.getId());
                psi.setTrainAlgorithmId(workflowTask.getAlgorithmId());
                psi.setTrainUserAddress(workflowRunStatus.getAddress());
                psi.setFilePath(filePath);
                psiList.add(psi);
            }
        }
        if(psiList.size() > 0){
            dataService.saveBatchPsi(psiList);
        }

        if(modelList.size() > 0){
            dataService.saveBatchModel(modelList);
        }

        if(taskResultList.size() > 0){
            workflowRunTaskResultManager.saveBatch(taskResultList);
        }
    }

    private CalculationProcess getCalculationProcessDetails(Long calculationProcessId) {
        CalculationProcess calculationProcess = calculationProcessManager.getById(calculationProcessId);
        calculationProcess.setStepItem(calculationProcessStepManager.getList(calculationProcessId));
        calculationProcess.setTaskItem(calculationProcessTaskManager.getList(calculationProcessId));
        return calculationProcess;
    }

    @Override
    @Transactional
    public WorkflowVersionKeyDto settingWorkflowOfWizardMode(WorkflowDetailsOfWizardModeDto req) {
        Integer step = req.getCalculationProcessStep().getStep();
        Workflow workflow = workflowManager.getById(req.getWorkflowId());
        checkWorkFlowOnlyOwner(workflow);
        // 更新工作流设置版本
        if(!workflow.getIsSettingCompleted()){
            workflowManager.updateStep(workflow.getWorkflowId(), step, calculationProcessStepManager.isEnd(workflow.getCalculationProcessId(), step));
        }
        // 设置参数
        WorkflowSettingWizard wizard = workflowSettingWizardManager.getOneByStep(req.getWorkflowId(), req.getWorkflowVersion(), step);

        // 查询算法树
        AlgorithmClassify root = algService.getAlgorithmClassifyTree(true);

        switch(req.getCalculationProcessStep().getType()){
            case INPUT_TRAINING:
                setInputOfWizardMode(req.getWorkflowId(), req.getWorkflowVersion(),
                        Optional.ofNullable(wizard.getTask1Step()), wizard.getTask2Step(),
                        root,
                        req.getTrainingInput().getIdentityId(), Optional.ofNullable(req.getTrainingInput().getIsPsi()), req.getTrainingInput().getItem(), Optional.empty(), Optional.empty(), Optional.empty());
                break;
            case PT_INPUT_TRAINING:
                setInputOfWizardMode(req.getWorkflowId(), req.getWorkflowVersion(),
                        Optional.empty(), wizard.getTask2Step(),
                        root,
                        req.getPtTrainingInput().getIdentityId(), Optional.empty(), Arrays.asList(req.getPtTrainingInput().getDataInput()), Optional.empty(), Optional.of(WorkflowTaskPowerTypeEnum.find(req.getPtTrainingInput().getPowerType())), Optional.of(req.getPtTrainingInput().getPowerIdentityId()));
                break;
            case INPUT_PREDICTION:
                Optional<String> modelId = Optional.empty();
                if(req.getPredictionInput().getModel() != null){
                    modelId = Optional.of(req.getPredictionInput().getModel().getMetaDataId());
                }
                setInputOfWizardMode(req.getWorkflowId(), req.getWorkflowVersion(),
                        Optional.ofNullable(wizard.getTask1Step()), wizard.getTask2Step(),
                        root,
                        req.getPredictionInput().getIdentityId(), Optional.ofNullable(req.getPredictionInput().getIsPsi()), req.getPredictionInput().getItem(), modelId, Optional.empty(), Optional.empty());
                break;
            case PT_INPUT_PREDICTION:
                Optional<String> modelId1 = Optional.empty();
                if(req.getPtPredictionInput().getModel() != null){
                    modelId1 = Optional.of(req.getPtPredictionInput().getModel().getMetaDataId());
                }
                setInputOfWizardMode(req.getWorkflowId(), req.getWorkflowVersion(),
                        Optional.empty(), wizard.getTask4Step(),
                        root,
                        req.getPtPredictionInput().getIdentityId(), Optional.empty(), Arrays.asList(req.getPtPredictionInput().getDataInput()), modelId1, Optional.of(WorkflowTaskPowerTypeEnum.find(req.getPtPredictionInput().getPowerType())), Optional.of(req.getPtPredictionInput().getPowerIdentityId()));
                break;
            case INPUT_PSI:
                setInputOfWizardMode(req.getWorkflowId(), req.getWorkflowVersion(),
                        Optional.empty(), wizard.getTask2Step(),
                        root,
                        req.getPsiInput().getIdentityId(), Optional.empty(), req.getPsiInput().getItem(), Optional.empty(), Optional.empty(), Optional.empty());
                break;
            case RESOURCE_COMMON:
                setCommonResourceOfWizardMode(req.getWorkflowId(), req.getWorkflowVersion(), Arrays.asList(wizard.getTask1Step(), wizard.getTask2Step()), req.getCommonResource());
                break;
            case RESOURCE_TRAINING_PREDICTION:
                setCommonResourceOfWizardMode(req.getWorkflowId(), req.getWorkflowVersion(), Arrays.asList(wizard.getTask1Step(), wizard.getTask2Step()), req.getTrainingAndPredictionResource().getTraining());
                setCommonResourceOfWizardMode(req.getWorkflowId(), req.getWorkflowVersion(), Arrays.asList(wizard.getTask3Step(), wizard.getTask4Step()), req.getTrainingAndPredictionResource().getPrediction());
                break;
            case OUTPUT_COMMON:
                setCommonOutputOfWizardMode(sysConfig.getDefaultPsi() == workflow.getAlgorithmId(), req.getWorkflowId(), req.getWorkflowVersion(), wizard.getTask2Step(), req.getCommonOutput());
                break;
            case OUTPUT_TRAINING_PREDICTION:
                setCommonOutputOfWizardMode(false, req.getWorkflowId(), req.getWorkflowVersion(), wizard.getTask2Step(), req.getTrainingAndPredictionOutput().getTraining());
                setCommonOutputOfWizardMode(false, req.getWorkflowId(), req.getWorkflowVersion(), wizard.getTask4Step(), req.getTrainingAndPredictionOutput().getPrediction());
                break;
        }
        WorkflowVersionKeyDto result = new WorkflowVersionKeyDto();
        result.setWorkflowId(req.getWorkflowId());
        result.setWorkflowVersion(req.getWorkflowVersion());
        return result;
    }

    @Override
    public WorkflowDetailsOfWizardModeDto getWorkflowSettingOfWizardMode(WorkflowWizardStepDto req) {
        WorkflowDetailsOfWizardModeDto result = new WorkflowDetailsOfWizardModeDto();
        Workflow workflow = workflowManager.getById(req.getWorkflowId());
        WorkflowSettingWizard wizard = workflowSettingWizardManager.getOneByStep(req.getWorkflowId(), req.getWorkflowVersion(), req.getStep());

        result.setWorkflowId(req.getWorkflowId());
        result.setWorkflowVersion(req.getWorkflowVersion());
        result.setAlgorithmId(workflow.getAlgorithmId());
        result.setCalculationProcessId(workflow.getCalculationProcessId());
        CalculationProcessStepDto calculationProcessStepDto = new CalculationProcessStepDto();
        calculationProcessStepDto.setStep(wizard.getStep());
        calculationProcessStepDto.setType(wizard.getCalculationProcessStepType());
        result.setCalculationProcessStep(calculationProcessStepDto);
        result.setWorkflowName(workflow.getWorkflowName());
        result.setWorkflowDesc(workflow.getWorkflowDesc());
        result.setIsSettingCompleted(workflow.getIsSettingCompleted());
        result.setCompletedCalculationProcessStep(workflow.getCalculationProcessStep());

        switch(wizard.getCalculationProcessStepType()){
            case INPUT_TRAINING:
                result.setTrainingInput(getTrainingInputOfWizardMode(req.getWorkflowId(), req.getWorkflowVersion(), wizard.getTask2Step()));
                break;
            case PT_INPUT_TRAINING:
                result.setPtTrainingInput(getPTTrainingInputOfWizardMode(req.getWorkflowId(), req.getWorkflowVersion(), wizard.getTask2Step()));
                break;
            case INPUT_PREDICTION:
                result.setPredictionInput(getPredictionInputOfWizardMode(req.getWorkflowId(), req.getWorkflowVersion(), wizard.getTask2Step()));
                break;
            case PT_INPUT_PREDICTION:
                result.setPtPredictionInput(getPTPredictionInputOfWizardMode(req.getWorkflowId(), req.getWorkflowVersion(), wizard.getTask4Step()));
                break;
            case INPUT_PSI:
                result.setPsiInput(getPsiInputOfWizardMode(req.getWorkflowId(), req.getWorkflowVersion(), wizard.getTask2Step()));
                break;
            case RESOURCE_COMMON:
                result.setCommonResource(getCommonResourceOfWizardMode(req.getWorkflowId(), req.getWorkflowVersion(), wizard.getTask2Step()));
                break;
            case RESOURCE_TRAINING_PREDICTION:
                result.setTrainingAndPredictionResource(getTrainingAndPredictionResourceOfWizardMode(req.getWorkflowId(), req.getWorkflowVersion(), wizard.getTask2Step(), wizard.getTask4Step()));
                break;
            case OUTPUT_COMMON:
                result.setCommonOutput(getCommonOutputOfWizardMode(req.getWorkflowId(), req.getWorkflowVersion(), wizard.getTask2Step()));
                break;
            case OUTPUT_TRAINING_PREDICTION:
                TrainingAndPredictionOutputDto trainingAndPredictionOutputDto = new TrainingAndPredictionOutputDto();
                trainingAndPredictionOutputDto.setTraining(getCommonOutputOfWizardMode(req.getWorkflowId(), req.getWorkflowVersion(), wizard.getTask2Step()));
                trainingAndPredictionOutputDto.setPrediction(getCommonOutputOfWizardMode(req.getWorkflowId(), req.getWorkflowVersion(), wizard.getTask4Step()));
                result.setTrainingAndPredictionOutput(trainingAndPredictionOutputDto);
                break;
        }
        return result;
    }

    private OutputDto getCommonOutputOfWizardMode(Long workflowId, Long workflowVersion, Integer taskStep) {
        OutputDto outputDto = new OutputDto();
        WorkflowTask workflowTask = workflowTaskManager.getByStep(workflowId, workflowVersion, taskStep);
        List<WorkflowTaskOutput> workflowTaskOutputList = workflowTaskOutputManager.listByWorkflowTaskId(workflowTask.getWorkflowTaskId());
        if(workflowTaskOutputList.size() > 0){
            outputDto.setStorePattern(workflowTaskOutputList.get(0).getStorePattern());
        }else{
            outputDto.setStorePattern(algService.getAlgorithm(workflowTask.getAlgorithmId(), false).getStorePattern());
        }
        outputDto.setIdentityId(workflowTaskOutputList.stream().map(WorkflowTaskOutput::getIdentityId).collect(Collectors.toList()));
        return outputDto;
    }

    private void setNodeOutput(Long workflowTaskId, OutputDto nodeOutput) {
        List<WorkflowTaskOutput> workflowTaskOutputList = convert2WorkflowTaskOutput(workflowTaskId, nodeOutput);
        workflowTaskOutputManager.saveBatch(workflowTaskOutputList);
    }

    private void setCommonOutputOfWizardMode(boolean checkPsi, Long workflowId, Long workflowVersion, Integer taskStep, OutputDto commonOutput) {
        WorkflowTask workflowTask = workflowTaskManager.getByStep(workflowId, workflowVersion, taskStep);
        List<WorkflowTaskOutput> workflowTaskOutputList = convert2WorkflowTaskOutput(workflowTask.getWorkflowTaskId(), commonOutput);
        // 如果是psi，输出必须包含在输入
        if(checkPsi){
            Set<String> inputIdSet = workflowTaskInputManager.listByWorkflowTaskId(workflowTask.getWorkflowTaskId())
                    .stream()
                    .map(WorkflowTaskInput::getIdentityId)
                    .collect(Collectors.toSet());

            Set<String> outputIdSet = workflowTaskOutputList.stream().map(WorkflowTaskOutput::getIdentityId).collect(Collectors.toSet());
            List<String> complement = outputIdSet.stream().filter(i -> !inputIdSet.contains(i))
                    .map(item -> orgService.getOrgById(item).getNodeName()).collect(Collectors.toList());
            if( complement.size() > 0) {
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, StringUtils.replace(ErrorMsg.WORKFLOW_SETTING_PSI_OUTERROR.getMsg(), "{}",complement.toString()));
            }
        }
        workflowTaskOutputManager.clearAndSave(workflowTask.getWorkflowTaskId(), workflowTaskOutputList);
    }

    private TrainingAndPredictionResourceDto getTrainingAndPredictionResourceOfWizardMode(Long workflowId, Long workflowVersion, Integer task1Step, Integer task2Step) {
        TrainingAndPredictionResourceDto trainingAndPredictionResourceDto = new TrainingAndPredictionResourceDto();
        trainingAndPredictionResourceDto.setTraining(getCommonResourceOfWizardMode(workflowId, workflowVersion, task1Step));
        trainingAndPredictionResourceDto.setPrediction(getCommonResourceOfWizardMode(workflowId, workflowVersion, task2Step));
        return trainingAndPredictionResourceDto;
    }

    private ResourceDto getCommonResourceOfWizardMode(Long workflowId, Long workflowVersion, Integer task1Step) {
        ResourceDto resourceDto = new ResourceDto();
        WorkflowTask resource = workflowTaskManager.getByStep(workflowId, workflowVersion, task1Step);
        WorkflowTaskResource workflowTaskResource = workflowTaskResourceManager.getById(resource.getWorkflowTaskId());

        resourceDto.setCostCpu(workflowTaskResource.getCostCpu());
        resourceDto.setCostGpu(workflowTaskResource.getCostGpu());
        resourceDto.setCostMem(CommonUtils.convert2UserOfCostMem(workflowTaskResource.getCostMem()));
        resourceDto.setCostBandwidth(CommonUtils.convert2UserOfCostBandwidth(workflowTaskResource.getCostBandwidth()));
        resourceDto.setRunTime(CommonUtils.convert2UserOfRunTime(workflowTaskResource.getRunTime()));
        return resourceDto;
    }


    private void setNodeResource(AlgorithmClassify algorithmClassify, WorkflowTask psiWorkflowTask, WorkflowTask workflowTask, ResourceDto resource) {
        List<Long> workflowTaskIdList = new ArrayList<>();
        workflowTaskIdList.add(workflowTask.getWorkflowTaskId());
        if(algorithmClassify.getAlg().getSupportDefaultPsi()){
            workflowTaskIdList.add(psiWorkflowTask.getWorkflowTaskId());
        }
        setCommonResource(workflowTaskIdList, resource);
    }

    private void setCommonResourceOfWizardMode(Long workflowId, Long workflowVersion, List<Integer> taskStepList, ResourceDto commonResource) {
        List<WorkflowTask> workflowTaskList = workflowTaskManager.listByWorkflowVersionAndSteps(workflowId, workflowVersion, taskStepList.stream()
                .filter(item -> item !=null)
                .collect(Collectors.toList()));
        List<Long> workflowTaskIdList = workflowTaskList.stream()
                .map(WorkflowTask::getWorkflowTaskId)
                .collect(Collectors.toList());
        setCommonResource(workflowTaskIdList, commonResource);
    }

    private void setCommonResource(List<Long> workflowTaskIdList, ResourceDto commonResource) {
        List<WorkflowTaskResource> workflowTaskResourceList = workflowTaskIdList.stream()
                .map(item -> {
                    WorkflowTaskResource workflowTaskResource = new WorkflowTaskResource();
                    workflowTaskResource.setWorkflowTaskId(item);
                    workflowTaskResource.setCostCpu(commonResource.getCostCpu());
                    workflowTaskResource.setCostGpu(commonResource.getCostGpu());
                    workflowTaskResource.setCostMem(CommonUtils.convert2DbOfCostMem(commonResource.getCostMem()));
                    workflowTaskResource.setCostBandwidth(CommonUtils.convert2DbOfCostBandwidth(commonResource.getCostBandwidth()));
                    workflowTaskResource.setRunTime(CommonUtils.convert2DbOfRunTime(commonResource.getRunTime()));
                    return workflowTaskResource;
                })
                .collect(Collectors.toList());
        workflowTaskResourceManager.clearAndSave(workflowTaskIdList, workflowTaskResourceList);
    }

    private PsiInputDto getPsiInputOfWizardMode(Long workflowId, Long workflowVersion, Integer task1Step) {
        PsiInputDto psiInputDto = new PsiInputDto();
        WorkflowTask psi = workflowTaskManager.getByStep(workflowId, workflowVersion, task1Step);
        psiInputDto.setIdentityId(psi.getIdentityId());
        psiInputDto.setItem(BeanUtil.copyToList(workflowTaskInputManager.listByWorkflowTaskId(psi.getWorkflowTaskId()), DataInputDto.class));
        return psiInputDto;
    }

    private PTPredictionInputDto getPTPredictionInputOfWizardMode(Long workflowId, Long workflowVersion, Integer taskStep) {
        PTPredictionInputDto predictionInputDto = new PTPredictionInputDto();
        WorkflowTask prediction = workflowTaskManager.getByStep(workflowId, workflowVersion, taskStep);
        predictionInputDto.setIdentityId(prediction.getIdentityId());
        predictionInputDto.setInputModel(prediction.getInputModel());
        predictionInputDto.setAlgorithmId(prediction.getAlgorithmId());
        if(prediction.getInputModel() && StringUtils.isNotBlank(prediction.getInputModelId())){
            predictionInputDto.setModel(BeanUtil.copyProperties(dataService.getModelById(prediction.getInputModelId()), ModelDto.class));
        }
        List<WorkflowTaskInput> workflowTaskInputList = workflowTaskInputManager.listByWorkflowTaskId(prediction.getWorkflowTaskId());
        if(workflowTaskInputList.size() > 0){
            predictionInputDto.setDataInput(BeanUtil.copyProperties(workflowTaskInputList.get(0), DataInputDto.class));
        }
        return predictionInputDto;
    }

    private PredictionInputDto getPredictionInputOfWizardMode(Long workflowId, Long workflowVersion, Integer taskStep) {
        PredictionInputDto predictionInputDto = new PredictionInputDto();
        WorkflowTask prediction = workflowTaskManager.getByStep(workflowId, workflowVersion, taskStep);
        predictionInputDto.setIdentityId(prediction.getIdentityId());
        predictionInputDto.setIsPsi(prediction.getInputPsi());
        predictionInputDto.setInputModel(prediction.getInputModel());
        predictionInputDto.setAlgorithmId(prediction.getAlgorithmId());
        if(prediction.getInputModel() && StringUtils.isNotBlank(prediction.getInputModelId())){
            predictionInputDto.setModel(BeanUtil.copyProperties(dataService.getModelById(prediction.getInputModelId()), ModelDto.class));
        }
        predictionInputDto.setItem(BeanUtil.copyToList(workflowTaskInputManager.listByWorkflowTaskId(prediction.getWorkflowTaskId()), DataInputDto.class));
        return predictionInputDto;
    }

    private PTTrainingInputDto getPTTrainingInputOfWizardMode(Long workflowId, Long workflowVersion, Integer task2Step) {
        PTTrainingInputDto trainingInputDto = new PTTrainingInputDto();
        WorkflowTask training = workflowTaskManager.getByStep(workflowId, workflowVersion, task2Step);
        trainingInputDto.setIdentityId(training.getIdentityId());
        trainingInputDto.setPowerType(training.getPowerType().getValue());
        trainingInputDto.setPowerIdentityId(training.getPowerIdentityId());
        List<WorkflowTaskInput> workflowTaskInputList = workflowTaskInputManager.listByWorkflowTaskId(training.getWorkflowTaskId());
        if(workflowTaskInputList.size() > 0){
            trainingInputDto.setDataInput(BeanUtil.copyProperties(workflowTaskInputList.get(0), DataInputDto.class));
        }
        return trainingInputDto;
    }

    private TrainingInputDto getTrainingInputOfWizardMode(Long workflowId, Long workflowVersion, Integer task2Step) {
        TrainingInputDto trainingInputDto = new TrainingInputDto();
        WorkflowTask training = workflowTaskManager.getByStep(workflowId, workflowVersion, task2Step);
        trainingInputDto.setIdentityId(training.getIdentityId());
        trainingInputDto.setIsPsi(training.getInputPsi());
        trainingInputDto.setItem(BeanUtil.copyToList(workflowTaskInputManager.listByWorkflowTaskId(training.getWorkflowTaskId()), DataInputDto.class));
        return trainingInputDto;
    }

    private void setInputOfWizardMode(Long workflowId, Long workflowVersion, Optional<Integer> prePsiTaskStep, Integer taskStep, AlgorithmClassify root, String senderIdentityId, Optional<Boolean> activationPrePsi, List<DataInputDto> dataInputDtoList, Optional<String> modelId, Optional<WorkflowTaskPowerTypeEnum> powerType, Optional<String> powerIdentityId) {
        // 设置主任务、前置PSI任务
        Map<WorkflowTaskInputTypeEnum, WorkflowTask> workflowTaskMap = workflowTaskManager.setWorkflowTask(workflowId, workflowVersion, prePsiTaskStep, taskStep, senderIdentityId, activationPrePsi, modelId, powerType, powerIdentityId);
        WorkflowTask workflowTask = workflowTaskMap.get(WorkflowTaskInputTypeEnum.NORMAL);
        WorkflowTask psiWorkflowTask = workflowTaskMap.get(WorkflowTaskInputTypeEnum.PSI);
        Optional<Long> prePsiWorkflowTaskId = psiWorkflowTask == null ? Optional.empty() : Optional.of(psiWorkflowTask.getWorkflowTaskId());

        // 设置主任务输入、前置PSI任务输入
        List<WorkflowTaskInput> workflowTaskInputList = convert2WorkflowTaskInput(workflowTask.getWorkflowTaskId(), dataInputDtoList);
        workflowTaskInputManager.setWorkflowTaskInput(prePsiWorkflowTaskId, workflowTask.getWorkflowTaskId(), workflowTaskInputList);

        // 设置前置PSI任务输出为数据的输入
        prePsiWorkflowTaskId.ifPresent(item -> {
            List<WorkflowTaskOutput> workflowTaskOutputList = convert2WorkflowTaskOutput(item, workflowTaskInputList, TreeUtils.findSubTree(root, psiWorkflowTask.getAlgorithmId()).getAlg());
            workflowTaskOutputManager.setWorkflowTaskOutput(item, workflowTaskOutputList);
        });
    }


    private void setNodeInput(AlgorithmClassify root, AlgorithmClassify algorithmClassify, WorkflowTask psiWorkflowTask, WorkflowTask workflowTask, NodeInputDto nodeInput) {
        setInputOfWizardMode(workflowTask.getWorkflowId(), workflowTask.getWorkflowVersion(),
                algorithmClassify.getAlg().getSupportDefaultPsi() ? Optional.ofNullable(psiWorkflowTask.getStep()) : Optional.empty(), workflowTask.getStep(),
                root,
                nodeInput.getIdentityId(), algorithmClassify.getAlg().getSupportDefaultPsi() ? Optional.ofNullable(nodeInput.getIsPsi()): Optional.empty(), nodeInput.getDataInputList(), algorithmClassify.getAlg().getInputModel() && nodeInput.getModel() !=null && !"frontNodeOutput".equals(nodeInput.getModel().getMetaDataId()) ? Optional.of(nodeInput.getModel().getMetaDataId()) : Optional.empty(), Optional.ofNullable(WorkflowTaskPowerTypeEnum.find(nodeInput.getPowerType())), Optional.ofNullable(nodeInput.getPowerIdentityId()));
    }

    @Override
    @Transactional
    public WorkflowVersionKeyDto settingWorkflowOfExpertMode(WorkflowDetailsOfExpertModeDto req) {
        Workflow dbWorkflow = workflowManager.getById(req.getWorkflowId());
        checkWorkFlowOnlyOwner(dbWorkflow);

        // 清理节点设置
        clearWorkflowOfExpertMode(req.getWorkflowId(), req.getWorkflowVersion());
        // 创建工作流任务配置
        List<WorkflowSettingExpert> workflowSettingExpertList = new ArrayList<>();
        int taskStep = 1;
        int preStep = 0;
        AlgorithmClassify root = algService.getAlgorithmClassifyTree(true);
        AlgorithmClassify psiAlgorithmClassify = TreeUtils.findSubTree(root, sysConfig.getDefaultPsi());
        for (int i = 0; i < req.getWorkflowNodeList().size(); i++) {
            NodeDto nodeDto = req.getWorkflowNodeList().get(i);
            // PSI的输出组织必须包含在输入中
            if(sysConfig.getDefaultPsi() == nodeDto.getAlgorithmId()){
                Set<String> inputIdSet = nodeDto.getNodeInput().getDataInputList()
                        .stream()
                        .map(DataInputDto::getIdentityId)
                        .collect(Collectors.toSet());
                List<String> outputIdSet = nodeDto.getNodeOutput().getIdentityId();
                List<String> complement = outputIdSet.stream().filter(subI -> !inputIdSet.contains(subI)).map(item -> orgService.getOrgById(item).getNodeName()).collect(Collectors.toList());
                if( complement.size() > 0) {
                    throw new BusinessException(RespCodeEnum.BIZ_FAILED, StringUtils.replace(ErrorMsg.WORKFLOW_SETTING_PSI_OUTERROR.getMsg(), "{}",complement.toString()));
                }
            }
            AlgorithmClassify algorithmClassify = TreeUtils.findSubTree(root, nodeDto.getAlgorithmId());
            // 创建psi任务
            WorkflowTask psiWorkflowTask = null;
            if(algorithmClassify.getAlg().getSupportDefaultPsi()){
                // 创建psi设置
                psiWorkflowTask = workflowTaskManager.createOfWizardMode(
                        req.getWorkflowId(), req.getWorkflowVersion(), taskStep++,
                        psiAlgorithmClassify.getId(), false, null,
                        false, null, Optional.empty());

                if(psiAlgorithmClassify.getAlg().getAlgorithmVariableList().size() > 0){
                    workflowTaskVariableManager.create(psiWorkflowTask.getWorkflowTaskId(), psiAlgorithmClassify.getAlg().getAlgorithmVariableList());
                }
            }

            // 创建任务
            WorkflowTask workflowTask = workflowTaskManager.createOfWizardMode(
                    req.getWorkflowId(), req.getWorkflowVersion(), taskStep++,
                    algorithmClassify.getId(), algorithmClassify.getAlg().getInputModel(),
                    algorithmClassify.getAlg().getInputModel() && ( nodeDto.getNodeInput().getModel() == null || "frontNodeOutput".equals(nodeDto.getNodeInput().getModel().getMetaDataId())) ? preStep : null,
                    nodeDto.getNodeInput().getIsPsi(), algorithmClassify.getAlg().getSupportDefaultPsi() ? psiWorkflowTask.getStep() : null,
                    algorithmClassify.getAlg().getType() == AlgorithmTypeEnum.PT ? Optional.of(WorkflowTaskPowerTypeEnum.RANDOM) : Optional.empty());

            // 创建设置
            WorkflowSettingExpert workflowSettingExpert = new WorkflowSettingExpert();
            workflowSettingExpert.setWorkflowId(req.getWorkflowId());
            workflowSettingExpert.setWorkflowVersion(req.getWorkflowVersion());
            workflowSettingExpert.setNodeStep(nodeDto.getNodeStep());
            workflowSettingExpert.setNodeName(nodeDto.getNodeName());
            workflowSettingExpert.setPsiTaskStep(algorithmClassify.getAlg().getSupportDefaultPsi()?psiWorkflowTask.getStep() : null);
            workflowSettingExpert.setTaskStep(workflowTask.getStep());
            workflowSettingExpertList.add(workflowSettingExpert);

            // 设置输入
            setNodeInput(root, algorithmClassify, psiWorkflowTask, workflowTask, nodeDto.getNodeInput());
            setNodeCode(workflowTask.getWorkflowTaskId(), nodeDto.getNodeCode());
            setNodeOutput(workflowTask.getWorkflowTaskId(),  nodeDto.getNodeOutput());
            setNodeResource(algorithmClassify, psiWorkflowTask, workflowTask, nodeDto.getResource());

            preStep = workflowTask.getStep();
        }
        // 创建节点定义
        workflowSettingExpertManager.saveBatch(workflowSettingExpertList);

        // 更新工作流状态
        Workflow workflow = new Workflow();
        workflow.setWorkflowId(req.getWorkflowId());
        workflow.setIsSettingCompleted(true);
        workflowManager.updateById(workflow);

        WorkflowVersionKeyDto result = new WorkflowVersionKeyDto();
        result.setWorkflowId(req.getWorkflowId());
        result.setWorkflowVersion(req.getWorkflowVersion());
        return result;
    }


    private void setNodeCode(Long workflowTaskId, NodeCodeDto nodeCode) {
        List<WorkflowTaskVariable> workflowTaskVariableList = nodeCode.getVariableList().stream()
                .map(item -> {
                    WorkflowTaskVariable workflowTaskVariable = new WorkflowTaskVariable();
                    workflowTaskVariable.setWorkflowTaskId(workflowTaskId);
                    workflowTaskVariable.setVarKey(item.getVarKey());
                    workflowTaskVariable.setVarValue(item.getVarValue());
                    return workflowTaskVariable;
                })
                .collect(Collectors.toList());
        workflowTaskVariableManager.saveBatch(workflowTaskVariableList);
    }


    private void clearWorkflowOfExpertMode(Long workflowId, Long workflowVersion) {
        // 清理工作流任务配置
        List<Long> workflowTaskIdList = workflowTaskManager.listByWorkflowVersion(workflowId, workflowVersion).stream()
                .map(WorkflowTask::getWorkflowTaskId)
                .collect(Collectors.toList());
        if(workflowTaskIdList.size() > 0){
            workflowTaskManager.removeByIds(workflowTaskIdList);
            workflowTaskInputManager.removeByWorkflowTaskIds(workflowTaskIdList);
            workflowTaskOutputManager.removeByWorkflowTaskIds(workflowTaskIdList);
            workflowTaskResourceManager.removeByIds(workflowTaskIdList);
            workflowTaskVariableManager.removeByWorkflowTaskIds(workflowTaskIdList);
        }
        // 清理节点定义
        workflowSettingExpertManager.removeByWorkflowVersion(workflowId, workflowVersion);
    }

    @Override
    public WorkflowDetailsOfExpertModeDto getWorkflowSettingOfExpertMode(WorkflowVersionKeyDto req) {
        WorkflowDetailsOfExpertModeDto result = new WorkflowDetailsOfExpertModeDto();
        Workflow workflow = workflowManager.getById(req.getWorkflowId());
        WorkflowVersion workflowVersion = workflowVersionManager.getById(req.getWorkflowId(), req.getWorkflowVersion());
        result.setWorkflowId(req.getWorkflowId());
        result.setWorkflowVersion(req.getWorkflowVersion());
        result.setWorkflowName(workflow.getWorkflowName());
        result.setStatus(workflowVersion.getStatus());
        result.setIsSettingCompleted(workflow.getIsSettingCompleted());
        result.setWorkflowNodeList(getWorkflowNodeList(req.getWorkflowId(), req.getWorkflowVersion()));
        return result;
    }

    private List<NodeDto> getWorkflowNodeList(Long workflowId, Long workflowVersion){
        List<NodeDto> nodeDtoList = workflowSettingExpertManager.listByWorkflowVersion(workflowId, workflowVersion).stream()
                .map(item -> {
                    NodeDto nodeDto = new NodeDto();
                    nodeDto.setNodeStep(item.getNodeStep());
                    nodeDto.setNodeName(item.getNodeName());
                    WorkflowTask workflowTask = workflowTaskManager.getByStep(workflowId, workflowVersion, item.getTaskStep());
                    Algorithm algorithm = algService.getAlgorithm(workflowTask.getAlgorithmId(), true);
                    nodeDto.setAlgorithmId(workflowTask.getAlgorithmId());
                    nodeDto.setAlg(BeanUtil.copyProperties(algorithm, AlgDto.class));
                    NodeCodeDto nodeCodeDto = new NodeCodeDto();
                    nodeCodeDto.setCode(BeanUtil.copyProperties(algorithm.getAlgorithmCode(),CodeDto.class));

                    Map<String,String> variableMap = workflowTaskVariableManager.listByWorkflowTaskId(workflowTask.getWorkflowTaskId())
                            .stream()
                            .collect(Collectors.toMap(WorkflowTaskVariable::getVarKey, WorkflowTaskVariable::getVarValue));
                    nodeCodeDto.setVariableList(algorithm.getAlgorithmVariableList().stream()
                            .map(algorithmVariable -> {
                                AlgVariableV2Dto algVariableDto = new AlgVariableV2Dto();
                                algVariableDto.setVarKey(algorithmVariable.getVarKey());
                                algVariableDto.setVarType(algorithmVariable.getVarType().getValue());
                                algVariableDto.setVarDesc(algorithmVariable.getVarDesc());
                                algVariableDto.setVarDescEn(algorithmVariable.getVarDescEn());
                                if(variableMap.containsKey(algorithmVariable.getVarKey())){
                                    algVariableDto.setVarValue(variableMap.get(algorithmVariable.getVarKey()));
                                }
                                return algVariableDto;
                            })
                            .collect(Collectors.toList()));
                    nodeDto.setNodeCode(nodeCodeDto);
                    NodeInputDto nodeInputDto = new NodeInputDto();
                    nodeInputDto.setIdentityId(workflowTask.getIdentityId());
                    nodeInputDto.setInputModel(workflowTask.getInputModel());
                    if(workflowTask.getInputModel()
                            && StringUtils.isNotBlank(workflowTask.getInputModelId())
                            && !"frontNodeOutput".equals(workflowTask.getInputModelId())){
                        nodeInputDto.setModel(BeanUtil.copyProperties(dataService.getModelById(workflowTask.getInputModelId()), ModelDto.class));
                    }
                    if(workflowTask.getInputModel()
                            && StringUtils.isBlank(workflowTask.getInputModelId())){
                        ModelDto modelDto = new ModelDto();
                        modelDto.setMetaDataId("frontNodeOutput");
                        nodeInputDto.setModel(modelDto);
                    }

                    nodeInputDto.setIsPsi(workflowTask.getInputPsi());
                    nodeInputDto.setDataInputList(BeanUtil.copyToList(workflowTaskInputManager.listByWorkflowTaskId(workflowTask.getWorkflowTaskId()), DataInputDto.class));
                    nodeInputDto.setPowerType(workflowTask.getPowerType() == null ? null : workflowTask.getPowerType().getValue());
                    nodeInputDto.setPowerIdentityId(workflowTask.getPowerIdentityId());
                    nodeDto.setNodeInput(nodeInputDto);

                    WorkflowTaskResource workflowTaskResource = workflowTaskResourceManager.getById(workflowTask.getWorkflowTaskId());
                    ResourceDto resourceDto = new ResourceDto();
                    resourceDto.setCostCpu(workflowTaskResource.getCostCpu());
                    resourceDto.setCostGpu(workflowTaskResource.getCostGpu());
                    resourceDto.setCostMem(CommonUtils.convert2UserOfCostMem(workflowTaskResource.getCostMem()));
                    resourceDto.setCostBandwidth(CommonUtils.convert2UserOfCostBandwidth(workflowTaskResource.getCostBandwidth()));
                    resourceDto.setRunTime(CommonUtils.convert2UserOfRunTime(workflowTaskResource.getRunTime()));
                    nodeDto.setResource(resourceDto);
                    OutputDto outputDto = new OutputDto();
                    List<WorkflowTaskOutput> workflowTaskOutputList = workflowTaskOutputManager.listByWorkflowTaskId(workflowTask.getWorkflowTaskId());
                    if(workflowTaskOutputList.size() > 0){
                        outputDto.setStorePattern(workflowTaskOutputList.get(0).getStorePattern());
                    }else{
                        outputDto.setStorePattern(algorithm.getStorePattern());
                    }
                    outputDto.setIdentityId(workflowTaskOutputList.stream().map(WorkflowTaskOutput::getIdentityId).collect(Collectors.toList()));
                    nodeDto.setNodeOutput(outputDto);
                    return nodeDto;
                })
                .collect(Collectors.toList());
        return nodeDtoList;
    }

    @Override
    public WorkflowStatusOfExpertModeDto getWorkflowStatusOfExpertMode(WorkflowVersionKeyDto req) {
        // 查询结构
        WorkflowStatusOfExpertModeDto result = new WorkflowStatusOfExpertModeDto();
        result.setWorkflowId(req.getWorkflowId());
        result.setWorkflowVersion(req.getWorkflowVersion());
        result.setRunStatus(WorkflowTaskRunStatusEnum.RUN_NEED);

        WorkflowRunStatus workflowRunStatus = workflowRunStatusManager.getLatestOneByWorkflowVersion(req.getWorkflowId(), req.getWorkflowVersion());
        if(workflowRunStatus != null && workflowRunStatus.getRunStatus() != null){
            result.setRunStatus(workflowRunStatus.getRunStatus());
        }

        List<WorkflowSettingExpert> workflowSettingExpertList = workflowSettingExpertManager.listByWorkflowVersion(req.getWorkflowId(), req.getWorkflowVersion());
        result.setWorkflowNodeStatusList(workflowSettingExpertList.stream()
                .map(item -> {
                    NodeStatusDto nodeStatusDto = new NodeStatusDto();
                    nodeStatusDto.setNodeStep(item.getNodeStep());
                    nodeStatusDto.setRunStatus(WorkflowTaskRunStatusEnum.RUN_NEED);
                    if(workflowRunStatus != null && workflowRunStatus.getRunStatus() != null){
                        nodeStatusDto.setRunStatus(getNodeRunStatus(workflowRunStatus.getId(), item.getPsiTaskStep(), item.getTaskStep()));
                    }
                    return nodeStatusDto;
                })
                .collect(Collectors.toList()));
        return result;
    }

    private WorkflowTaskRunStatusEnum getNodeRunStatus(Long id, Integer psiTaskStep, Integer taskStep) {
        if(psiTaskStep != null){
           WorkflowRunStatusTask psiWorkflowRunTaskStatus = workflowRunTaskStatusManager.getByWorkflowRunIdAndStep(id, psiTaskStep);
           if(psiWorkflowRunTaskStatus != null && psiWorkflowRunTaskStatus.getRunStatus() == WorkflowTaskRunStatusEnum.RUN_FAIL){
                return WorkflowTaskRunStatusEnum.RUN_FAIL;
           }
        }
        WorkflowRunStatusTask workflowRunTaskStatus = workflowRunTaskStatusManager.getByWorkflowRunIdAndStep(id, taskStep);
        if(workflowRunTaskStatus.getRunStatus() == WorkflowTaskRunStatusEnum.RUN_FAIL || workflowRunTaskStatus.getRunStatus() == WorkflowTaskRunStatusEnum.RUN_SUCCESS){
            return workflowRunTaskStatus.getRunStatus();
        }
        return WorkflowTaskRunStatusEnum.RUN_DOING;
    }

    @Override
    public List<TaskEventDto> getWorkflowLogOfExpertMode(WorkflowVersionKeyDto req) {
        List<TaskEventDto> result = new ArrayList<>();
        WorkflowRunStatus workflowRunStatus = workflowRunStatusManager.getLatestOneByWorkflowVersion(req.getWorkflowId(), req.getWorkflowVersion());
        if(workflowRunStatus == null){
            return result;
        }

        Map<String, Org> identityId2OrgMap = orgService.getIdentityId2OrgMap();
        result = workflowRunTaskStatusManager.listByWorkflowRunIdAndHasTaskId(workflowRunStatus.getId()).stream()
                .flatMap(item -> {
                    WorkflowTask workflowTask = workflowTaskManager.getById(item.getWorkflowTaskId());
                   return taskService.listTaskEventByTaskId(item.getTaskId(),  workflowTask.getIdentityId()).stream();
                })
                .map(item -> {
                    TaskEventDto taskEventDto = new TaskEventDto();
                    BeanUtil.copyProperties(item, taskEventDto);
                    taskEventDto.setNodeName(identityId2OrgMap.get(taskEventDto.getIdentityId()).getNodeName());
                    return taskEventDto;
                })
                .collect(Collectors.toList());
        return result;
    }

    @Override
    public List<TaskResultDto> getWorkflowNodeResult(WorkflowNodeKeyDto req) {
        List<TaskResultDto> result = new ArrayList<>();
        WorkflowRunStatus workflowRunStatus = workflowRunStatusManager.getLatestOneByWorkflowVersion(req.getWorkflowId(), req.getWorkflowVersion());
        if(workflowRunStatus == null){
            return result;
        }

        WorkflowSettingExpert workflowSettingExpert = workflowSettingExpertManager.getByWorkflowVersionAndStep(req.getWorkflowId(), req.getWorkflowVersion(), req.getNodeStep());
        Set<Integer> stepSet = new HashSet<>();
        stepSet.add(workflowSettingExpert.getTaskStep());
        if(workflowSettingExpert.getPsiTaskStep() != null){
            stepSet.add(workflowSettingExpert.getPsiTaskStep());
        }

        Map<String, Org> identityId2OrgMap = orgService.getIdentityId2OrgMap();
        result = workflowRunTaskStatusManager.listByWorkflowRunIdAndHasTaskId(workflowRunStatus.getId()).stream()
                .filter(item -> stepSet.contains(item.getStep()))
                .map(WorkflowRunStatusTask::getTaskId)
                .flatMap(item -> workflowRunTaskResultManager.listByTaskId(item).stream())
                .map(item -> {
                    TaskResultDto taskResultDto = new TaskResultDto();
                    BeanUtil.copyProperties(item, taskResultDto);
                    taskResultDto.setOrg(BeanUtil.copyProperties(identityId2OrgMap.get(item.getIdentityId()), OrgNameDto.class));
                    return taskResultDto;
                })
                .collect(Collectors.toList());
        return result;
    }

    @Override
    @Transactional
    public WorkflowVersionKeyDto copyWorkflow(WorkflowVersionNameDto req) {
        checkWorkFlowVersionName(req.getWorkflowId(), req.getWorkflowVersionName());
        WorkflowVersionKeyDto result = new WorkflowVersionKeyDto();
        // 更新工作流对象
        Workflow workflow = workflowManager.increaseVersion(req.getWorkflowId());
        checkWorkFlowOnlyOwner(workflow);
        // 创建工作流版本
        workflowVersionManager.create(workflow.getWorkflowId(), workflow.getWorkflowVersion(), req.getWorkflowVersionName());
        // 复制设置的信息
        if(workflow.getCreateMode() == WorkflowCreateModeEnum.EXPERT_MODE){
            workflowSettingExpertManager.copyAndReset(workflow.getWorkflowId(), req.getWorkflowVersion(), workflow.getWorkflowVersion());
        }else{
            workflowSettingWizardManager.copyAndReset(workflow.getWorkflowId(), req.getWorkflowVersion(), workflow.getWorkflowVersion());
        }
        // 复制任务设置
        List<Map<OldAndNewEnum, WorkflowTask>> workflowTaskList = workflowTaskManager.copy(workflow.getWorkflowId(), req.getWorkflowVersion(), workflow.getWorkflowVersion());

        workflowTaskList.forEach(item -> {
            workflowTaskInputManager.copy(item.get(OldAndNewEnum.OLD).getWorkflowTaskId(), item.get(OldAndNewEnum.NEW).getWorkflowTaskId());
            workflowTaskOutputManager.copy(item.get(OldAndNewEnum.OLD).getWorkflowTaskId(), item.get(OldAndNewEnum.NEW).getWorkflowTaskId());
            workflowTaskResourceManager.copy(item.get(OldAndNewEnum.OLD).getWorkflowTaskId(), item.get(OldAndNewEnum.NEW).getWorkflowTaskId());
            workflowTaskVariableManager.copy(item.get(OldAndNewEnum.OLD).getWorkflowTaskId(), item.get(OldAndNewEnum.NEW).getWorkflowTaskId());
        });

        result.setWorkflowId(workflow.getWorkflowId());
        result.setWorkflowVersion(workflow.getWorkflowVersion());
        return result;
    }

    @Override
    @Transactional
    public Boolean deleteWorkflow(WorkflowKeyDto req) {
        // 检查否存在运行记录
        if(workflowRunStatusManager.hasBeenRun(req.getWorkflowId())){
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_BEEN_RUN.getMsg());
        }
        // 删除工作流
        Workflow workflow = workflowManager.delete(req.getWorkflowId());
        // 删除工作流版本
        workflowVersionManager.deleteByWorkflowId(workflow.getWorkflowId());
        // 删除设置的信息
        if(workflow.getCreateMode() == WorkflowCreateModeEnum.EXPERT_MODE){
            workflowSettingExpertManager.deleteWorkflowId(workflow.getWorkflowId());
        }else{
            workflowSettingWizardManager.deleteWorkflowId(workflow.getWorkflowId());
        }
        // 复制任务设置
        List<WorkflowTask> workflowTaskList = workflowTaskManager.deleteWorkflowId(workflow.getWorkflowId());

        workflowTaskList.forEach(item -> {
            workflowTaskInputManager.deleteByWorkflowTaskId(item.getWorkflowTaskId());
            workflowTaskOutputManager.deleteByWorkflowTaskId(item.getWorkflowTaskId());
            workflowTaskResourceManager.deleteByWorkflowTaskId(item.getWorkflowTaskId());
            workflowTaskVariableManager.deleteByWorkflowTaskId(item.getWorkflowTaskId());
        });
        return true;
    }

    @Override
    public List<WorkflowStartCredentialDto> preparationStartCredentialList(WorkflowVersionKeyDto req) {
        // 查询可执行的任务列表
        List<WorkflowTask> workflowTaskList = listExecutableDetailsByWorkflowVersion(req.getWorkflowId(), req.getWorkflowVersion());
        // 获得可执行的任务列表使用的元数据列表
        Set<String> metaDataIdSet = workflowTaskList.stream()
                .flatMap(item -> item.getInputList().stream())
                .map(item -> item.getMetaDataId())
                .collect(Collectors.toSet());
//        // 过滤掉自己的元数据，因为自己发布的元数据不需要走支付
//        metaDataIdSet = metaDataIdSet.stream()
//                .filter(item -> !dataService.isMetaDataOwner(item))
//                .collect(Collectors.toSet());
        // 查询元数据对应用户的凭证
        List<WorkflowStartCredentialDto> result = metaDataIdSet.stream()
                .map(item -> {
                    WorkflowStartCredentialDto workflowStartCredentialDto = new WorkflowStartCredentialDto();
                    workflowStartCredentialDto.setMetaDataId(item);
                    workflowStartCredentialDto.setMetaDataName(dataService.getMetaDataName(item));
                    workflowStartCredentialDto.setNoAttributesCredential(BeanUtil.copyProperties(dataService.getNoAttributeCredentialByMetaDataIdAndUser(item), NoAttributesCredentialDto.class));
                    workflowStartCredentialDto.setHaveAttributesCredentialList(BeanUtil.copyToList(dataService.listHaveAttributesCertificateByMetaDataIdAndUser(item), HaveAttributesCredentialDto.class));
                    return workflowStartCredentialDto;
                })
                .collect(Collectors.toList());
        return result;
    }

    @Override
    public WorkflowFeeDto preparationStart(WorkflowVersionKeyAndCredentialIdListDto req) {
        List<WorkflowTask> workflowTaskList = listExecutableDetailsByWorkflowVersion(req.getWorkflowId(), req.getWorkflowVersion());
        WorkflowFeeDto workflowFeeDto = new WorkflowFeeDto();
        workflowFeeDto.setWorkflowId(req.getWorkflowId());
        workflowFeeDto.setWorkflowVersion(req.getWorkflowVersion());
        workflowFeeDto.setItemList(convert2WorkflowFee(workflowTaskList, dataService.listMetaDataCertificateUser(req.getCredentialIdList())));
        return workflowFeeDto;
    }

    @Transactional
    @Override
    public WorkflowRunKeyDto start(WorkflowStartSignatureDto req) {
        // 工作流启动校验
        // 1. 运行状态校验
        WorkflowRunStatus lastWorkflowRunStatus = workflowRunStatusManager.getLatestOneByWorkflowVersion(req.getWorkflowId(), req.getWorkflowVersion());
        if(lastWorkflowRunStatus != null && lastWorkflowRunStatus.getRunStatus() == WorkflowTaskRunStatusEnum.RUN_DOING){
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_RUNNING_EXIST.getMsg());
        }

        // 生成运行时任务清单明细
        List<WorkflowTask> workflowTaskList = listExecutableDetailsByWorkflowVersion(req.getWorkflowId(), req.getWorkflowVersion());
        // 交易-手续费校验
        List<MetaDataCertificate> metaDataCertificateList = dataService.listMetaDataCertificateUser(req.getCredentialIdList());
        List<WorkflowFeeItemDto> workflowFeeList = convert2WorkflowFee(workflowTaskList, metaDataCertificateList);
        checkFee(workflowFeeList);
        // 保存运行时信息
        WorkflowRunStatus workflowRunStatus = createAndSaveWorkflowRunStatus(UserContext.getCurrentUser().getAddress(), req.getSign(), req.getWorkflowId(), req.getWorkflowVersion(), workflowTaskList, metaDataCertificateList);
        // 更新运行时间
        workflowManager.updateLastRunTime(workflowRunStatus.getWorkflowId());
        // 启动任务
        executeTask(workflowRunStatus);
        // 返回
        WorkflowRunKeyDto result = new WorkflowRunKeyDto();
        result.setWorkflowRunId(workflowRunStatus.getId());
        return result;
    }

    private void executeTask(WorkflowRunStatus workflowRunStatus) {
        WorkflowRunStatusTask curWorkflowRunTaskStatus = workflowRunStatus.getWorkflowRunTaskStatusList().stream().collect(Collectors.toMap(WorkflowRunStatusTask::getStep, item -> item)).get(workflowRunStatus.getCurStep());

        if(curWorkflowRunTaskStatus.getRunStatus() == WorkflowTaskRunStatusEnum.RUN_NEED){
            try {
                // 运行时初始化依赖
                initModelAndPsi(curWorkflowRunTaskStatus);

                curWorkflowRunTaskStatus.setRunStatus(WorkflowTaskRunStatusEnum.RUN_DOING);
                curWorkflowRunTaskStatus.setBeginTime(new Date());
                // 提交任务到 Net
                TaskRpcApi.PublishTaskDeclareRequest request = assemblyTask(workflowRunStatus, curWorkflowRunTaskStatus);

                TaskRpcApi.PublishTaskDeclareResponse response = grpcTaskServiceClient.publishTaskDeclare(orgService.getChannel(curWorkflowRunTaskStatus.getWorkflowTask().getIdentityId()), request);
                curWorkflowRunTaskStatus.setTaskId(response.getTaskId());
                curWorkflowRunTaskStatus.setRunMsg(response.getMsg());
                if (GrpcConstant.GRPC_SUCCESS_CODE != response.getStatus()) {
                    curWorkflowRunTaskStatus.setRunStatus(WorkflowTaskRunStatusEnum.RUN_FAIL);
                    curWorkflowRunTaskStatus.setEndTime(new Date());
                    workflowRunStatus.setRunStatus(WorkflowTaskRunStatusEnum.RUN_FAIL);
                    workflowRunStatus.setEndTime(new Date());
                }
                log.info("任务发布结果>>>>工作流id:{},任务名称：{},rosettanet收到处理任务，返回的taskId：{}", workflowRunStatus.getId(), request.getTaskName(), response.getTaskId());
            } catch (Exception e){
                log.error("executeTask error! runStatusId = {}  runTaskStatusId = {}", curWorkflowRunTaskStatus.getId());
                log.error("executeTask error! ", e);
                curWorkflowRunTaskStatus.setRunMsg(e.getMessage());
                curWorkflowRunTaskStatus.setRunStatus(WorkflowTaskRunStatusEnum.RUN_FAIL);
                curWorkflowRunTaskStatus.setEndTime(new Date());
                workflowRunStatus.setRunStatus(WorkflowTaskRunStatusEnum.RUN_FAIL);
                workflowRunStatus.setEndTime(new Date());
            }
            workflowRunStatusManager.updateById(workflowRunStatus);
            workflowRunTaskStatusManager.updateById(curWorkflowRunTaskStatus);
        }

    }

    private TaskRpcApi.PublishTaskDeclareRequest assemblyTask(WorkflowRunStatus workflowRunStatus, WorkflowRunStatusTask curWorkflowRunTaskStatus) {
        // 全局参数
        WorkflowTask workflowTask =  curWorkflowRunTaskStatus.getWorkflowTask();
        String calculateContractStruct = workflowTask.getAlgorithm().getAlgorithmCode().getCalculateContractStruct();
        List<WorkflowTaskVariable> workflowTaskVariableList = workflowTask.getVariableList();
        Optional<WorkflowTaskInput> dependentVariableWorkflowTaskInput = workflowTask.getInputList().stream().filter(item -> item.getDependentVariable() != null && item.getDependentVariable() > 0).findFirst();
        Algorithm algorithm = workflowTask.getAlgorithm();
        List<WorkflowTaskInput> workflowTaskInputList = workflowTask.getInputList();
        List<WorkflowTaskOutput> workflowTaskOutputList = workflowTask.getOutputList();
        Org senderOrg = workflowTask.getOrg();

        // 组装任务提交参数
        TaskRpcApi.PublishTaskDeclareRequest.Builder requestBuild = TaskRpcApi.PublishTaskDeclareRequest.newBuilder()
                .setTaskName(publishTaskOfGetTaskName(workflowRunStatus, curWorkflowRunTaskStatus))
                .setUser(workflowRunStatus.getAddress())
                .setUserType(CarrierEnum.UserType.User_1)
                .setSender(publishTaskOfGetTaskOrganization(senderOrg, "sender1"))
                .setAlgoSupplier(publishTaskOfGetTaskOrganization(senderOrg, "algo1"));

        // 设置数据输入组织
        workflowTaskInputList.forEach(workflowTaskInput -> {
            requestBuild.addDataSuppliers(publishTaskOfGetTaskOrganization(workflowTaskInput.getOrg(), workflowTaskInput.getPartyId()));
            if(workflowTask.getInputPsi()){
                requestBuild.addDataPolicyTypes(TaskDataPolicyTypesEnum.POLICY_TYPES_30001.getValue());
                requestBuild.addDataPolicyOptions(createDataPolicyItem(workflowTaskInput, curWorkflowRunTaskStatus.getPreStepTaskId()));
            }else{
                requestBuild.addDataPolicyTypes(TaskDataPolicyTypesEnum.POLICY_TYPES_40001.getValue());
                requestBuild.addDataPolicyOptions(createDataPolicyItem(workflowTaskInput, workflowRunStatus.getMetaDataId2WorkflowRunStatusCertificate(), algorithm.getType()));
            }
        });

        // 设置模型输入组织
        String modelPartyId = null;
        if(workflowTask.getInputModel()){
            modelPartyId = "model1";
            requestBuild.addDataSuppliers(publishTaskOfGetTaskOrganization(curWorkflowRunTaskStatus.getModel().getOrg(), modelPartyId));
            requestBuild.addDataPolicyTypes(TaskDataPolicyTypesEnum.POLICY_TYPES_2.getValue());
            requestBuild.addDataPolicyOptions(createDataPolicyItem(curWorkflowRunTaskStatus.getModel(), modelPartyId));
        }

        // 接收方策略
        workflowTaskOutputList.forEach(workflowTaskOutput -> {
            requestBuild.addReceivers(publishTaskOfGetTaskOrganization(workflowTaskOutput.getOrg(), workflowTaskOutput.getPartyId()));
            if(algorithm.getAlgorithmId() == sysConfig.getDefaultPsi()){
                requestBuild.addReceiverPolicyTypes(TaskReceiverPolicyTypesEnum.POLICY_TYPES_2.getValue());
                requestBuild.addReceiverPolicyOptions(createReceiverPolicyItem(workflowTaskOutput, workflowTaskInputList));
            } else {
                requestBuild.addReceiverPolicyTypes(TaskReceiverPolicyTypesEnum.POLICY_TYPES_1.getValue());
                requestBuild.addReceiverPolicyOptions(workflowTaskOutput.getPartyId());
            }
        });

        // 算力策略
        if(algorithm.getAlgorithmId() == sysConfig.getDefaultPsi()){
            // psi指定数据节点提供算力策略
            workflowTaskInputList.forEach(workflowTaskInput -> {
                requestBuild.addPowerPolicyTypes(TaskPowerPolicyTypesEnum.POLICY_TYPES_2.getValue());
                requestBuild.addPowerPolicyOptions(createPowerPolicyItem(workflowTaskInput));
            });
        } else if(algorithm.getType() == AlgorithmTypeEnum.PT && workflowTask.getPowerType() == WorkflowTaskPowerTypeEnum.ASSIGN){
            // 明文算法并且算力用户指定算力
            requestBuild.addPowerPolicyTypes(TaskPowerPolicyTypesEnum.POLICY_TYPES_3.getValue());
            requestBuild.addPowerPolicyOptions(createPowerPolicyItem("compute1", curWorkflowRunTaskStatus.getWorkflowTask().getIdentityId()));
        } else if(algorithm.getType() == AlgorithmTypeEnum.PT && workflowTask.getPowerType() == WorkflowTaskPowerTypeEnum.RANDOM){
            // 明文算法并且算力随机选1个
            requestBuild.addPowerPolicyTypes(TaskPowerPolicyTypesEnum.POLICY_TYPES_1.getValue());
            requestBuild.addPowerPolicyOptions("compute1");
        } else {
            // 随机算力
            requestBuild.addPowerPolicyTypes(TaskPowerPolicyTypesEnum.POLICY_TYPES_1.getValue());
            requestBuild.addPowerPolicyOptions("compute1");
            requestBuild.addPowerPolicyTypes(TaskPowerPolicyTypesEnum.POLICY_TYPES_1.getValue());
            requestBuild.addPowerPolicyOptions("compute2");
            requestBuild.addPowerPolicyTypes(TaskPowerPolicyTypesEnum.POLICY_TYPES_1.getValue());
            requestBuild.addPowerPolicyOptions("compute3");
        }

        JSONObject dataFlowPolicyOption;
        TaskDataFlowPolicyTypesEnum dataFlowPolicyType;
        // 连接策略
        if(algorithm.getAlgorithmId() == sysConfig.getDefaultPsi()){
            /**
             * psi网络连接:
             *
             * data1             data2
             *    ↓                 ↓
             * compute1 -------> compute2
             *          <-------
             *    ↓                 ↓
             *  result1           result2
             */
            dataFlowPolicyType = TaskDataFlowPolicyTypesEnum.POLICY_TYPES_2;
            dataFlowPolicyOption = createDataFlowPolicy(workflowTaskInputList, workflowTaskOutputList);
        } else if(algorithm.getType() == AlgorithmTypeEnum.PT && algorithm.getInputModel() == false){
            /**
             * 明文训练算法网络连接:
             *
             * data1
             *   ↓
             * compute1
             *   ↓
             * result1 （只需要指定一个结果接收方，多了业务逻辑意义）
             */
            dataFlowPolicyType = TaskDataFlowPolicyTypesEnum.POLICY_TYPES_2;
            dataFlowPolicyOption = createDataFlowPolicy(workflowTaskInputList.get(0).getPartyId(), Optional.empty(), workflowTaskOutputList.stream().map(WorkflowTaskOutput::getPartyId).collect(Collectors.toList()));
        } else if(algorithm.getType() == AlgorithmTypeEnum.PT && algorithm.getInputModel() == false){
            /**
             * 明文预测算法网络连接:
             *
             * data1       model1
             *   ↓          ↓
             * compute1
             *   ↓
             * result1
             */
            dataFlowPolicyType = TaskDataFlowPolicyTypesEnum.POLICY_TYPES_2;
            dataFlowPolicyOption = createDataFlowPolicy(workflowTaskInputList.get(0).getPartyId(), Optional.of(modelPartyId), workflowTaskOutputList.stream().map(WorkflowTaskOutput::getPartyId).collect(Collectors.toList()));
        } else {
            // 其他全连接
            dataFlowPolicyType = TaskDataFlowPolicyTypesEnum.POLICY_TYPES_1;
            dataFlowPolicyOption = createDataFlowPolicy();
        }
        requestBuild.addDataFlowPolicyTypes(dataFlowPolicyType.getValue());
        requestBuild.addDataFlowPolicyOptions(dataFlowPolicyOption.toJSONString());

        // 设置资源使用
        WorkflowTaskResource resource = workflowTask.getResource();
        Resourcedata.TaskResourceCostDeclare taskResourceCostDeclare = Resourcedata.TaskResourceCostDeclare.newBuilder()
                .setMemory(resource.getCostMem())
                .setProcessor(resource.getCostCpu())
                .setBandwidth(resource.getCostBandwidth())
                .setDuration(resource.getRunTime())
                .build();
        requestBuild.setOperationCost(taskResourceCostDeclare);

        // 设置算法代码
        requestBuild.setAlgorithmCode(algorithm.getAlgorithmCode().getCalculateContractCode());
        requestBuild.setMetaAlgorithmId(algorithm.getType() == AlgorithmTypeEnum.CT ? "ciphertext" : "plaintext");

        // 设置算法变量
        boolean useAlignment =  workflowTaskInputList.stream()
                .filter(item-> StringUtils.isNotBlank(item.getDataColumnIds()))
                .count() > 0 ? true : false;
        requestBuild.setAlgorithmCodeExtraParams(createAlgorithmCodeExtraParams(calculateContractStruct, useAlignment, workflowTask.getInputPsi(), dependentVariableWorkflowTaskInput, Optional.ofNullable(modelPartyId), workflowTaskVariableList, algorithm, dataFlowPolicyOption));

        // 其他设置
        requestBuild.setSign(ByteString.copyFromUtf8(workflowRunStatus.getSign()));
        requestBuild.setDesc("");
        return requestBuild.build();
    }

    private JSONObject createDataFlowPolicy() {
        return new JSONObject();
    }

    private JSONObject createDataFlowPolicy(String inputPartId, Optional<String> modelPartIdOptional, List<String> outputPartIdList) {
        JSONObject connectPolicy = new JSONObject();
        // data -> compute 连接
        JSONArray computeArray = new JSONArray();
        computeArray.add("compute1");
        connectPolicy.put(inputPartId, computeArray);
        // model -> compute 连接
        modelPartIdOptional.ifPresent(modelPartId -> connectPolicy.put(modelPartId, computeArray));
        // compute -> result 连接
        JSONArray resultArray = new JSONArray();
        outputPartIdList.forEach(resultPartId -> resultArray.add(resultPartId));
        connectPolicy.put("compute1", resultArray);
        return connectPolicy;
    }

    private JSONObject createDataFlowPolicy(List<WorkflowTaskInput> inputList, List<WorkflowTaskOutput> outputList) {
        JSONObject connectPolicy = new JSONObject();
        // data -> compute 连接
        Map<String, WorkflowTaskInput> workflowTaskInputMap = new HashMap<>();
        for (WorkflowTaskInput workflowTaskInput : inputList) {
            JSONArray value1 = new JSONArray();
            value1.add(StringUtils.replace(workflowTaskInput.getPartyId(), "data", "compute"));
            connectPolicy.put(workflowTaskInput.getPartyId(),value1 );
            workflowTaskInputMap.put(workflowTaskInput.getIdentityId(), workflowTaskInput);
        }
        // compute -> compute  compute -> result 连接
        Map<String, WorkflowTaskOutput> workflowTaskOutputMap = outputList.stream().collect(Collectors.toMap(WorkflowTaskOutput::getIdentityId, me -> me));
        for (WorkflowTaskInput workflowTaskInput : inputList) {
            JSONArray value1 = new JSONArray();
            workflowTaskInputMap.keySet().stream().filter(item -> ! workflowTaskInput.getIdentityId().equals(item)).forEach(item -> {
                value1.add(StringUtils.replace(workflowTaskInputMap.get(item).getPartyId(), "data", "compute"));
            });
            if(workflowTaskOutputMap.containsKey(workflowTaskInput.getIdentityId())){
                value1.add(workflowTaskOutputMap.get(workflowTaskInput.getIdentityId()).getPartyId());
            }
            connectPolicy.put(StringUtils.replace(workflowTaskInput.getPartyId(), "data", "compute"),value1);
        }
        return connectPolicy;
    }

    private String createAlgorithmCodeExtraParams(String calculateContractStruct, Boolean useAlignment, Boolean usePsi, Optional<WorkflowTaskInput> dependentVariableWorkflowTaskInputOptional, Optional<String> modelPartyIdOptional, List<WorkflowTaskVariable> variableList, Algorithm algorithm,JSONObject dataFlowRestrict) {
        JSONObject algorithmDynamicParams = JSONObject.parseObject(calculateContractStruct);
        // psi时是否对齐
        if(algorithmDynamicParams.containsKey("use_alignment")){
            algorithmDynamicParams.put("use_alignment", useAlignment);
        }
        //是否使用psi
        if(algorithmDynamicParams.containsKey("use_psi")){
            algorithmDynamicParams.put("use_psi", usePsi);
        }
        //标签所在方的party_id
        if(algorithmDynamicParams.containsKey("label_owner")){
            dependentVariableWorkflowTaskInputOptional.ifPresent(workflowTaskInput -> algorithmDynamicParams.put("label_owner", workflowTaskInput.getPartyId()));
        }
        // 因变量(标签)
        if(algorithmDynamicParams.containsKey("label_column")){
            dependentVariableWorkflowTaskInputOptional.ifPresent(workflowTaskInput -> algorithmDynamicParams.put("label_column", dataService.getDataColumnByIds(workflowTaskInput.getMetaDataId(), workflowTaskInput.getDependentVariable().intValue()).getColumnName()));
        }
        // 模型所在方
        if(algorithmDynamicParams.containsKey("model_restore_party")){
            modelPartyIdOptional.ifPresent(modelPartyId -> algorithmDynamicParams.put("model_restore_party", modelPartyId));
        }
        // 动态参数
        if(algorithmDynamicParams.containsKey("hyperparams")){
            JSONObject hyperParams = algorithmDynamicParams.getJSONObject("hyperparams");
            Map<String, AlgorithmVariableTypeEnum> variableTypeEnumMap = algorithm.getAlgorithmVariableList().stream().collect(Collectors.toMap(AlgorithmVariable::getVarKey, AlgorithmVariable::getVarType));
            variableList.forEach(workflowTaskVariable -> hyperParams.put(workflowTaskVariable.getVarKey(), convert2HyperParams(workflowTaskVariable, variableTypeEnumMap)));
        }
        // 连接方式
        if(algorithmDynamicParams.containsKey("data_flow_restrict")){
            algorithmDynamicParams.put("data_flow_restrict", dataFlowRestrict);
        }
        return algorithmDynamicParams.toJSONString();
    }

    private Object convert2HyperParams(WorkflowTaskVariable workflowTaskVariable, Map<String, AlgorithmVariableTypeEnum> variableTypeEnumMap) {
        switch (variableTypeEnumMap.get(workflowTaskVariable.getVarKey())) {
            case BOOLEAN:
                return Boolean.valueOf(workflowTaskVariable.getVarValue());
            case NUMBER:
                return new BigDecimal(workflowTaskVariable.getVarValue());
            case NUMBER_ARRAY:
                JSONArray numberResult = new JSONArray();
                Arrays.stream(workflowTaskVariable.getVarValue().split(",")).forEach(value -> {
                    numberResult.add(new BigDecimal(value));
                });
                return numberResult;
            case STRING_ARRAY:
                JSONArray stringResult = new JSONArray();
                Arrays.stream(workflowTaskVariable.getVarValue().split(",")).forEach(value -> {
                    stringResult.add(value);
                });
                return stringResult;
            default:
                return workflowTaskVariable.getVarValue();
        }
    }

    private String createReceiverPolicyItem(WorkflowTaskOutput workflowTaskOutput, List<WorkflowTaskInput> inputList) {
        TaskReceiverPolicy taskReceiverPolicy = new TaskReceiverPolicy();
        taskReceiverPolicy.setProviderPartyId(inputList.stream().filter(item -> item.getIdentityId().equals(workflowTaskOutput.getIdentityId())).findFirst().get().getPartyId());
        taskReceiverPolicy.setReceiverPartyId(workflowTaskOutput.getPartyId());
        return JSONObject.toJSONString(taskReceiverPolicy);

    }

    private String createPowerPolicyItem(String partyId, String identityId) {
        TaskPowerPolicy taskPowerPolicy = new TaskPowerPolicy();
        taskPowerPolicy.setPowerPartyId(partyId);
        taskPowerPolicy.setIdentityId(identityId);
        return JSONObject.toJSONString(taskPowerPolicy);
    }

    private String createPowerPolicyItem(WorkflowTaskInput workflowTaskInput) {
        TaskPowerPolicy taskPowerPolicy = new TaskPowerPolicy();
        taskPowerPolicy.setProviderPartyId(workflowTaskInput.getPartyId());
        taskPowerPolicy.setPowerPartyId(StringUtils.replace(workflowTaskInput.getPartyId(), "data", "compute"));
        return JSONObject.toJSONString(taskPowerPolicy);
    }

    private String createDataPolicyItem(Model model, String partyId) {
        TaskDataPolicyUnknown dataPolicy = new TaskDataPolicyUnknown();
        dataPolicy.setInputType(2);
        dataPolicy.setPartyId(partyId);
        dataPolicy.setMetadataId(model.getMetaDataId());
        dataPolicy.setMetadataName(model.getName());
        return JSONObject.toJSONString(dataPolicy);
    }

    private String createDataPolicyItem(WorkflowTaskInput workflowTaskInput, Map<String, WorkflowRunStatusCertificate> metaDataId2WorkflowRunStatusCertificate, AlgorithmTypeEnum algorithmType) {
        TaskDataPolicyCsv dataPolicy = new TaskDataPolicyCsv();
        dataPolicy.setInputType(1);
        dataPolicy.setPartyId(workflowTaskInput.getPartyId());
        dataPolicy.setMetadataId(workflowTaskInput.getMetaDataId());
        dataPolicy.setMetadataName(dataService.getMetaDataById(workflowTaskInput.getMetaDataId(), false).getMetaDataName());
        dataPolicy.setKeyColumn(workflowTaskInput.getKeyColumn());
        List<Integer> selectedColumns = new ArrayList<>();
        if(StringUtils.isNotBlank(workflowTaskInput.getDataColumnIds())){
            Arrays.stream(workflowTaskInput.getDataColumnIds().split(",")).forEach(subItem -> {
                selectedColumns.add(Integer.valueOf(subItem));
            });
        }
        dataPolicy.setSelectedColumns(selectedColumns);
        WorkflowRunStatusCertificate workflowRunStatusCertificate = metaDataId2WorkflowRunStatusCertificate.get(workflowTaskInput.getMetaDataId());
        TaskDataPolicyHaveConsume.Consume consume = new TaskDataPolicyHaveConsume.Consume();
        consume.setTokenAddress(workflowRunStatusCertificate.getTokenAddress());
        if(workflowRunStatusCertificate.getType() == MetaDataCertificateTypeEnum.NO_ATTRIBUTES){
            consume.setConsumeType(2);
            consume.setBalance(algorithmType == AlgorithmTypeEnum.CT ? workflowRunStatusCertificate.getErc20CtAlgConsume() : workflowRunStatusCertificate.getErc20PtAlgConsume());
        } else {
            consume.setConsumeType(3);
            consume.setTokenId(workflowRunStatusCertificate.getTokenId());
        }
        dataPolicy.setConsume(consume);
        return JSONObject.toJSONString(dataPolicy);
    }

    private String createDataPolicyItem(WorkflowTaskInput workflowTaskInput, String preStepTaskId) {
        TaskDataPolicyPreTask dataPolicy = new TaskDataPolicyPreTask();
        dataPolicy.setInputType(1);
        dataPolicy.setPartyId(workflowTaskInput.getPartyId());
        dataPolicy.setTaskId(preStepTaskId);

        List<Integer> selectedColumnsV2 = new ArrayList<>();
        List<Integer> userSelect = new ArrayList<>();
        userSelect.add(workflowTaskInput.getKeyColumn().intValue());
        if(StringUtils.isNotBlank(workflowTaskInput.getDataColumnIds())){
            Arrays.stream(workflowTaskInput.getDataColumnIds().split(",")).forEach(subItem -> {
                userSelect.add(Integer.valueOf(subItem));
                selectedColumnsV2.add(Integer.valueOf(subItem));
            });
        }

        List<MetaDataColumn> metaDataColumnList = dataService.listMetaDataColumnByIdAndIndex(workflowTaskInput.getMetaDataId(), userSelect);
        Map<Integer, MetaDataColumn> metaDataColumnMap = metaDataColumnList.stream().collect(Collectors.toMap(MetaDataColumn::getColumnIdx, me -> me));

        dataPolicy.setKeyColumnName(metaDataColumnMap.get(workflowTaskInput.getKeyColumn().intValue()).getColumnName());

        List<String> selectedColumnsName = new ArrayList<>();
        selectedColumnsV2.forEach(item -> {
            selectedColumnsName.add(metaDataColumnMap.get(item).getColumnName());
        });

        dataPolicy.setSelectedColumnNames(selectedColumnsName);
        return JSONObject.toJSONString(dataPolicy);
    }

    private Taskdata.TaskOrganization publishTaskOfGetTaskOrganization(Org identity, String partyId){
        Taskdata.TaskOrganization taskOrganization = Taskdata.TaskOrganization.newBuilder()
                .setPartyId(partyId)
                .setNodeName(identity.getNodeName())
                .setNodeId(identity.getNodeId())
                .setIdentityId(identity.getIdentityId())
                .build();
        return taskOrganization;
    }

    private String publishTaskOfGetTaskName(WorkflowRunStatus workflowRunStatus, WorkflowRunStatusTask curWorkflowRunTaskStatus) {
        Long id = curWorkflowRunTaskStatus.getId();
        String address = workflowRunStatus.getAddress();
        String algorithmName = curWorkflowRunTaskStatus.getWorkflowTask().getAlgorithm().getAlgorithmName();
        String workflowVersionName = workflowVersionManager.getById(workflowRunStatus.getWorkflowId(), workflowRunStatus.getWorkflowVersion()).getWorkflowVersionName();
        WorkflowCreateModeEnum createModeEnum = workflowRunStatus.getWorkflow().getCreateMode();
        return StringUtils.abbreviate(StringUtils.joinWith("_", id, address, algorithmName, createModeEnum.getValue(),workflowVersionName), 100);
    }

    private WorkflowRunStatus loadWorkflowRunStatus(WorkflowRunStatus workflowRunStatus, List<WorkflowTask> workflowTaskList) {
        Map<Long, WorkflowTask> workflowTaskMap = workflowTaskList.stream().collect(Collectors.toMap(WorkflowTask::getWorkflowTaskId, me -> me));
        workflowRunStatus.setWorkflow(workflowManager.getById(workflowRunStatus.getWorkflowId()));
        List<WorkflowRunStatusTask> workflowRunTaskStatusList = workflowRunTaskStatusManager.listByWorkflowRunId(workflowRunStatus.getId());
        workflowRunTaskStatusList
                .forEach(item -> {
                    item.setWorkflowTask(workflowTaskMap.get(item.getWorkflowTaskId()));
                });
        workflowRunStatus.setWorkflowRunTaskStatusList(workflowRunTaskStatusList);
        workflowRunStatus.setWorkflowRunStatusCertificateList(workflowRunStatusCertificateManager.listByWorkflowRunId(workflowRunStatus.getId()));

        return workflowRunStatus;
    }

    private WorkflowRunStatus createAndSaveWorkflowRunStatus(String address, String sign, Long workflowId, Long workflowVersion, List<WorkflowTask> workflowTaskList, List<MetaDataCertificate> metaDataCertificateList) {
        WorkflowRunStatus workflowRunStatus = new WorkflowRunStatus();
        workflowRunStatus.setWorkflowId(workflowId);
        workflowRunStatus.setWorkflowVersion(workflowVersion);
        workflowRunStatus.setSign(sign);
        workflowRunStatus.setAddress(address);
        workflowRunStatus.setStep(workflowTaskList.get(workflowTaskList.size() - 1).getStep());
        workflowRunStatus.setCurStep(workflowTaskList.get(0).getStep());
        workflowRunStatus.setRunStatus(WorkflowTaskRunStatusEnum.RUN_DOING);
        workflowRunStatus.setWorkflow(workflowManager.getById(workflowId));
        workflowRunStatusManager.save(workflowRunStatus);

        List<WorkflowRunStatusTask> workflowRunTaskStatusList = workflowTaskList.stream()
                .map(item -> {
                    WorkflowRunStatusTask workflowRunTaskStatus = new WorkflowRunStatusTask();
                    workflowRunTaskStatus.setWorkflowRunId(workflowRunStatus.getId());
                    workflowRunTaskStatus.setWorkflowTaskId(item.getWorkflowTaskId());
                    workflowRunTaskStatus.setStep(item.getStep());
                    workflowRunTaskStatus.setRunStatus(WorkflowTaskRunStatusEnum.RUN_NEED);
                    workflowRunTaskStatus.setModelId(item.getInputModelId());
                    workflowRunTaskStatus.setWorkflowTask(item);
                    workflowRunTaskStatusManager.save(workflowRunTaskStatus);
                    return workflowRunTaskStatus;
                })
                .collect(Collectors.toList());
        workflowRunStatus.setWorkflowRunTaskStatusList(workflowRunTaskStatusList);

        List<WorkflowRunStatusCertificate> workflowRunStatusCertificateList = metaDataCertificateList.stream().map(item -> {
            WorkflowRunStatusCertificate workflowRunStatusCertificate = new WorkflowRunStatusCertificate();
            workflowRunStatusCertificate.setWorkflowRunId(workflowRunStatus.getId());
            workflowRunStatusCertificate.setMetaDataId(item.getMetaDataId());
            workflowRunStatusCertificate.setType(item.getType());
            workflowRunStatusCertificate.setTokenAddress(item.getTokenAddress());
            workflowRunStatusCertificate.setTokenId(item.getTokenId());
            workflowRunStatusCertificate.setErc20CtAlgConsume(item.getErc20CtAlgConsume());
            workflowRunStatusCertificate.setErc20PtAlgConsume(item.getErc20PtAlgConsume());
            workflowRunStatusCertificateManager.save(workflowRunStatusCertificate);
            return workflowRunStatusCertificate;
        }).collect(Collectors.toList());
        workflowRunStatus.setWorkflowRunStatusCertificateList(workflowRunStatusCertificateList);
        return workflowRunStatus;
    }

    /**
     * 检验用户账户余额是否足够执行任务
     *
     * @param workflowFeeList
     */
    private void checkFee(List<WorkflowFeeItemDto> workflowFeeList) {
        List<String> tokenNameList = workflowFeeList.stream()
                .filter(item -> !item.getIsEnough())
                .map(item -> item.getToken().getName())
                .collect(Collectors.toList());
        if( tokenNameList.size() > 0) {
   }
    }

    private List<WorkflowFeeItemDto> convert2WorkflowFee(List<WorkflowTask> workflowTaskList, List<MetaDataCertificate> metaDataCertificateList) {
        // 如果任务中存在使用非自己元数据并且使用无属性凭证支付方式，则需要进行发起组织白名单校验
        Map<String, MetaDataCertificate> metaDataId2credentialKeyDtoMap = metaDataCertificateList.stream().collect(Collectors.toMap(MetaDataCertificate::getMetaDataId, me -> me));
        Map<String, Org> orgIdentityId2OrgMap = orgService.getUserOrgList().stream().collect(Collectors.toMap(Org::getIdentityId, me -> me));
        Set<String> needCheckSet = new HashSet<>();
        for (WorkflowTask workflowTask: workflowTaskList) {
            if(needCheckSet.contains(workflowTask.getIdentityId())){
                continue;
            }
            if(needWhitelistAuthorization(workflowTask, metaDataId2credentialKeyDtoMap)){
                needCheckSet.add(workflowTask.getIdentityId());
            }
        }
        List<String> errorSenderList = new ArrayList<>();
        for (String sender : needCheckSet){
            if(!orgIdentityId2OrgMap.containsKey(sender) || ! orgIdentityId2OrgMap.get(sender).getIsInWhitelist()){
                if(orgIdentityId2OrgMap.containsKey(sender)){
                    errorSenderList.add(orgIdentityId2OrgMap.get(sender).getNodeName());
                }else{
                    errorSenderList.add(sender);
                }
            }
        }
        if(errorSenderList.size() > 0){
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, StringUtils.replace(ErrorMsg.ORGANIZATION_NOT_IN_WHITE_LIST.getMsg(), "{}", errorSenderList.toString()));
        }

        // 如果是无属性凭证支付，需要保证余额及授权金额足够支付任务消耗； 如果是有属性凭证支付，则需要该凭证的拥有者为当前发起任务的用户
        List<WorkflowFeeItemDto> tokenFeeList = new ArrayList<>();
        Map<String, BigInteger> metaDataId2consumptionMap = new HashMap<>();
        for (WorkflowTask workflowTask: workflowTaskList) {
            for (WorkflowTaskInput taskInput: workflowTask.getInputList()) {
                MetaDataCertificate metaDataCertificate = metaDataId2credentialKeyDtoMap.get(taskInput.getMetaDataId());
                if(metaDataCertificate.getType() == MetaDataCertificateTypeEnum.HAVE_ATTRIBUTES){
                    metaDataId2consumptionMap.put(taskInput.getMetaDataId(), BigInteger.ZERO);
                }
                if(metaDataCertificate.getType() == MetaDataCertificateTypeEnum.NO_ATTRIBUTES){
                    metaDataId2consumptionMap.computeIfPresent(taskInput.getMetaDataId(), (key, oldValue) -> {
                        if ( workflowTask.getAlgorithm().getType() == AlgorithmTypeEnum.CT){
                            return oldValue.add(new BigInteger(metaDataCertificate.getErc20CtAlgConsume()));
                        }else{
                            return oldValue.add(new BigInteger(metaDataCertificate.getErc20PtAlgConsume()));
                        }
                    });
                }
            }
        }
        List<String> errorCertificateList = new ArrayList<>();
        metaDataId2consumptionMap.entrySet().forEach(item -> {
            MetaDataCertificate metaDataCertificate = metaDataId2credentialKeyDtoMap.get(item.getKey());
            if(item.getValue().compareTo(new BigInteger(metaDataCertificate.getTokenBalance())) > 0
            || item.getValue().compareTo(new BigInteger(metaDataCertificate.getAuthorizeBalance())) > 0){
                errorCertificateList.add(metaDataCertificate.getTokenName());
            }
            if(metaDataCertificate.getType() == MetaDataCertificateTypeEnum.NO_ATTRIBUTES){
                tokenFeeList.add(createWorkflowTaskFeeItemDto(WorkflowPayTypeEnum.TOKEN, item.getValue().toString(), metaDataCertificate));
            }
        });
        if(errorSenderList.size() > 0){
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, StringUtils.replace(ErrorMsg.WORKFLOW_EXECUTE_VALUE_INSUFFICIENT.getMsg(), "{}",errorCertificateList.toString()));
        }

        // 手续费
        UserWLatCredentialDto wLatCredentialDto = dataService.getUserWLatCredential();
        BigInteger feeList = workflowTaskList.stream()
                .map(item -> {
                    TaskRpcApi.EstimateTaskGasRequest.Builder requestBuilder = TaskRpcApi.EstimateTaskGasRequest.newBuilder();
                    requestBuilder.setTaskSponsorAddress(UserContext.getCurrentUser().getAddress());
                    item.getInputList().stream().map(WorkflowTaskInput::getMetaDataId).forEach(metaDataId -> {
                        MetaDataCertificate metaDataCertificate = metaDataId2credentialKeyDtoMap.get(metaDataId);
                        TaskRpcApi.TokenItem.Builder tokenItem = TaskRpcApi.TokenItem.newBuilder();
                        tokenItem.setTokenType(metaDataCertificate.getType() == MetaDataCertificateTypeEnum.NO_ATTRIBUTES ? TokenEnum.TokenType.ERC20 : TokenEnum.TokenType.ERC721);
                        tokenItem.setTokenAddress(metaDataCertificate.getTokenAddress());
                        if(metaDataCertificate.getType() == MetaDataCertificateTypeEnum.NO_ATTRIBUTES){
                            tokenItem.setValue(item.getAlgorithm().getType() == AlgorithmTypeEnum.CT? Long.valueOf(metaDataCertificate.getErc20CtAlgConsume()): Long.valueOf(metaDataCertificate.getErc20PtAlgConsume()));
                        }else{
                            tokenItem.setId(Long.valueOf(metaDataCertificate.getTokenId()));
                        }
                        requestBuilder.addTokenItems(tokenItem);
                    });

                    TaskRpcApi.EstimateTaskGasResponse response = grpcTaskServiceClient.estimateTaskGas(orgService.getChannel(item.getIdentityId()), requestBuilder.build());
                    return BigInteger.valueOf(response.getGasLimit()).multiply(BigInteger.valueOf(response.getGasPrice()));
                })
                .reduce((item1, item2) -> item1.add(item2) ).get();
        tokenFeeList.add(createWorkflowTaskFeeItemDto(WorkflowPayTypeEnum.FEE, feeList.toString(),wLatCredentialDto));
        return tokenFeeList;
    }

    private boolean needWhitelistAuthorization(WorkflowTask workflowTask, Map<String, MetaDataCertificate> credentialKeyMap) {
        for (WorkflowTaskInput taskInput: workflowTask.getInputList()) {
            MetaData metaData = dataService.getMetaDataById(taskInput.getMetaDataId(), false);
            if(!UserContext.getCurrentUser().getAddress().equals(metaData.getOwnerAddress()) && credentialKeyMap.get(metaData.getMetaDataId()).getType() == MetaDataCertificateTypeEnum.NO_ATTRIBUTES){
                return true;
            }
        }
        return false;
    }

    private WorkflowFeeItemDto createWorkflowTaskFeeItemDto(WorkflowPayTypeEnum typeEnum, String needValue, UserWLatCredentialDto metaDataCertificate){
        WorkflowFeeItemDto workflowFeeItemDto = new WorkflowFeeItemDto();
        workflowFeeItemDto.setType(typeEnum);
        workflowFeeItemDto.setNeedValue(needValue);
        workflowFeeItemDto.setToken(BeanUtil.copyProperties(metaDataCertificate, TokenDto.class));
        workflowFeeItemDto.setTokenHolder(BeanUtil.copyProperties(metaDataCertificate, TokenHolderDto.class));
        workflowFeeItemDto.setIsEnough(
                new BigDecimal(workflowFeeItemDto.getTokenHolder().getBalance()).compareTo(new BigDecimal(workflowFeeItemDto.getNeedValue())) >= 0
                        && new BigDecimal(workflowFeeItemDto.getTokenHolder().getAuthorizeBalance()).compareTo(new BigDecimal(workflowFeeItemDto.getNeedValue())) >= 0);
        return workflowFeeItemDto;
    }

    private WorkflowFeeItemDto createWorkflowTaskFeeItemDto(WorkflowPayTypeEnum typeEnum, String needValue, MetaDataCertificate metaDataCertificate){
        WorkflowFeeItemDto workflowFeeItemDto = new WorkflowFeeItemDto();
        workflowFeeItemDto.setType(typeEnum);
        workflowFeeItemDto.setNeedValue(needValue);
        workflowFeeItemDto.setToken(BeanUtil.copyProperties(metaDataCertificate, TokenDto.class));
        workflowFeeItemDto.setTokenHolder(BeanUtil.copyProperties(metaDataCertificate, TokenHolderDto.class));
        workflowFeeItemDto.setIsEnough(
                new BigDecimal(workflowFeeItemDto.getTokenHolder().getBalance()).compareTo(new BigDecimal(workflowFeeItemDto.getNeedValue())) >= 0
                        && new BigDecimal(workflowFeeItemDto.getTokenHolder().getAuthorizeBalance()).compareTo(new BigDecimal(workflowFeeItemDto.getNeedValue())) >= 0);
        return workflowFeeItemDto;
    }


    private void initModelAndPsi(WorkflowRunStatusTask curWorkflowRunTaskStatus) {
        WorkflowTask workflowTask = curWorkflowRunTaskStatus.getWorkflowTask();
        // 初始化模型
        if(workflowTask.getInputModel()){
            if(StringUtils.isBlank(workflowTask.getInputModelId())){
                // 上步骤动态生成
                curWorkflowRunTaskStatus.setModel(dataService.getModelByOrgAndTrainTaskId(workflowTask.getIdentityId(), workflowRunTaskStatusManager.getByWorkflowRunIdAndStep(curWorkflowRunTaskStatus.getWorkflowRunId(), workflowTask.getInputModelStep()).getTaskId()));
            } else {
                // 用户输入
                curWorkflowRunTaskStatus.setModel(dataService.getModelById(workflowTask.getInputModelId()));
            }

            // 模型的组织
            curWorkflowRunTaskStatus.getModel().setOrg(orgService.getOrgById(curWorkflowRunTaskStatus.getModel().getIdentityId()));
        }

        // 初始化PSI
        if(workflowTask.getInputPsi()){
            String taskId = workflowRunTaskStatusManager.getByWorkflowRunIdAndStep(curWorkflowRunTaskStatus.getWorkflowRunId(), workflowTask.getInputPsiStep()).getTaskId();
            curWorkflowRunTaskStatus.setPreStepTaskId(taskId);
        }
    }

    private List<WorkflowTask> listExecutableDetailsByWorkflowVersion(Long workflowId, Long workflowVersion){
        List<WorkflowTask> workflowTaskList = workflowTaskManager.listExecutableByWorkflowVersion(workflowId, workflowVersion);
        workflowTaskList.stream().forEach(item ->{
            // 设置 input
            item.setInputList(workflowTaskInputManager.listByWorkflowTaskId(item.getWorkflowTaskId()));
            // 设置 output
            item.setOutputList(workflowTaskOutputManager.listByWorkflowTaskId(item.getWorkflowTaskId()));
            // 设置 resource
            item.setResource(workflowTaskResourceManager.getById(item.getWorkflowTaskId()));
            // 设置 variable
            item.setVariableList(workflowTaskVariableManager.listByWorkflowTaskId(item.getWorkflowTaskId()));
            // 设置 算法
            item.setAlgorithm(algService.getAlgorithm(item.getAlgorithmId(), true));
            // 设置 发起组织
            item.setOrg(orgService.getOrgById(item.getIdentityId()));
            // 设置 input 组织
            item.getInputList().forEach(subItem -> {
                subItem.setOrg(orgService.getOrgById(subItem.getIdentityId()));
            });
            // 设置 output 组织
            item.getOutputList().forEach(subItem -> {
                subItem.setOrg(orgService.getOrgById(subItem.getIdentityId()));
            });
        });
        return workflowTaskList;
    }

    @Override
    public Boolean terminate(WorkflowRunKeyDto req) {
        WorkflowRunStatus workflowRunStatus = workflowRunStatusManager.getById(req.getWorkflowRunId());
        Workflow workflow = workflowManager.getById(workflowRunStatus.getWorkflowId());
        // 只有拥有者才可以终止
        checkWorkFlowOnlyOwner(workflow);
        // 只有运行中的才可以终止
        if (workflowRunStatus.getRunStatus() != WorkflowTaskRunStatusEnum.RUN_DOING) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NOT_RUNNING.getMsg());
        }
        // 工作流取消中
        if (workflowRunStatus.getCancelStatus() == WorkflowTaskRunStatusEnum.RUN_NEED) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_CANCELING.getMsg());
        }
        // 工作流已取消
        if (workflowRunStatus.getCancelStatus() == WorkflowTaskRunStatusEnum.RUN_SUCCESS) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_CANCELLED_SUCCESS.getMsg());
        }
        // 工作流已取消
        if (workflowRunStatus.getCancelStatus() == WorkflowTaskRunStatusEnum.RUN_FAIL) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_CANCELLED_FAIL.getMsg());
        }
        // 更新工作流运行状态
        workflowRunStatus.setCancelStatus(WorkflowTaskRunStatusEnum.RUN_NEED);
        return workflowRunStatusManager.updateById(workflowRunStatus);
    }

    @Override
    public List<WorkflowRunTaskDto> getWorkflowRunTaskList(WorkflowRunKeyDto req) {
        WorkflowRunStatus workflowRunStatus = workflowRunStatusManager.getById(req.getWorkflowRunId());
        List<WorkflowRunStatusTask> workflowRunTaskStatusList = workflowRunTaskStatusManager.listByWorkflowRunId(req.getWorkflowRunId());
        WorkflowVersion workflowVersion = workflowVersionManager.getById(workflowRunStatus.getWorkflowId(), workflowRunStatus.getWorkflowVersion());
        AlgorithmClassify root = algService.getAlgorithmClassifyTree(false);
        List<WorkflowRunTaskDto> result = workflowRunTaskStatusList.stream().map(item -> {
            WorkflowRunTaskDto workflowRunTaskDto = new WorkflowRunTaskDto();
            workflowRunTaskDto.setId(item.getId());
            workflowRunTaskDto.setTaskId(item.getTaskId());
            workflowRunTaskDto.setCreateTime(item.getBeginTime());
            WorkflowTask workflowTask = workflowTaskManager.getById(item.getWorkflowTaskId());
            AlgorithmClassify algorithmClassify = TreeUtils.findSubTree(root, workflowTask.getAlgorithmId());
            workflowRunTaskDto.setAlgorithmName(algorithmClassify.getName());
            workflowRunTaskDto.setAlgorithmNameEn(algorithmClassify.getNameEn());
            workflowRunTaskDto.setStatus(item.getRunStatus());
            workflowRunTaskDto.setWorkflowVersionName(workflowVersion.getWorkflowVersionName());
            workflowRunTaskDto.setOutputModel(algorithmClassify.getAlg().getOutputModel());
            return workflowRunTaskDto;
        }).collect(Collectors.toList());
        return result;
    }

    @Override
    public WorkflowRunTaskResultDto getWorkflowRunTaskResult(String taskId) {
        WorkflowRunTaskResultDto resultDto = new WorkflowRunTaskResultDto();
        WorkflowRunStatusTask workflowRunTaskStatus = workflowRunTaskStatusManager.getByTaskId(taskId);
        resultDto.setId(workflowRunTaskStatus.getId());
        resultDto.setTaskId(workflowRunTaskStatus.getTaskId());
        resultDto.setCreateTime(workflowRunTaskStatus.getBeginTime());
        WorkflowTask workflowTask = workflowTaskManager.getById(workflowRunTaskStatus.getWorkflowTaskId());
        Algorithm algorithm = algService.getAlgorithm(workflowTask.getAlgorithmId(), false);
        WorkflowVersion workflowVersion = workflowVersionManager.getById(workflowTask.getWorkflowId(), workflowTask.getWorkflowVersion());
        resultDto.setAlgorithmName(algorithm.getAlgorithmName());
        resultDto.setAlgorithmNameEn(algorithm.getAlgorithmNameEn());
        resultDto.setStatus(workflowRunTaskStatus.getRunStatus());
        resultDto.setWorkflowVersionName(workflowVersion.getWorkflowVersionName());
        resultDto.setOutputModel(algorithm.getOutputModel());
        resultDto.setEndAt(workflowRunTaskStatus.getEndTime());
        resultDto.setTaskResultList(BeanUtil.copyToList(listWorkflowRunTaskResultByTaskId(resultDto.getTaskId()), TaskResultDto.class));
        resultDto.setEventList(BeanUtil.copyToList(taskService.listTaskEventByTaskId(resultDto.getTaskId(), workflowTask.getIdentityId()), TaskEventDto.class));
        if(algorithm.getOutputModel()){
            Model model = dataService.getModelByTaskId(resultDto.getTaskId());
            if(model != null){
                resultDto.setModelEvaluate(model.getEvaluate());
            }
        }
        return resultDto;
    }

    private List<WorkflowRunTaskResult> listWorkflowRunTaskResultByTaskId(String taskId){
        List<WorkflowRunTaskResult> workflowRunTaskResultList = workflowRunTaskResultManager.listByTaskId(taskId);
        if(workflowRunTaskResultList.size() == 0){
            return workflowRunTaskResultList;
        }

        Map<String, Org> orgMap = orgService.getIdentityId2OrgMap();
        workflowRunTaskResultList.forEach(item -> {
            if(orgMap.containsKey(item.getIdentityId())){
                item.setOrg(orgMap.get(item.getIdentityId()));
            }
            JSONObject o = JSONObject.parseObject(item.getMetadataOption());
            if(o.containsKey("dataPath")){
                item.setFilePath(o.getString("dataPath"));
            }
            if(o.containsKey("dirPath")){
                item.setFilePath(o.getString("dirPath"));
            }
        });

        return workflowRunTaskResultList;
    }


    private void checkWorkFlowOnlyOwner(Workflow workflow){
        // 只有拥有者才可以终止
        if(!workflow.getAddress().equals(UserContext.getCurrentUser().getAddress())){
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_ONLY_OWNER_OPERATE.getMsg());
        }
    }

    private void checkWorkFlowName(String workflowName){
        List<Workflow> workflowList = workflowManager.listByNameAndAddress(UserContext.getCurrentUser().getAddress(), workflowName);
        if(workflowList.size() > 0){
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NAME_EXIST.getMsg());
        }
    }

    private void checkWorkFlowVersionName(Long workflowId, String workflowVersionName) {
        List<WorkflowVersion> workflowVersionList = workflowVersionManager.listByNameAndId(workflowId, workflowVersionName);
        if(workflowVersionList.size() > 0){
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_VERSION_NAME_EXIST.getMsg());
        }
    }

    private List<WorkflowTaskInput> convert2WorkflowTaskInput(Long workflowTaskId, List<DataInputDto> dataInputDtoList){
        List<WorkflowTaskInput> workflowTaskInputList = new ArrayList<>();
        for (int i = 0; i < dataInputDtoList.size(); i++) {
            WorkflowTaskInput workflowTaskInput = new WorkflowTaskInput();
            workflowTaskInput.setWorkflowTaskId(workflowTaskId);
            workflowTaskInput.setMetaDataId(dataInputDtoList.get(i).getMetaDataId());
            workflowTaskInput.setIdentityId(dataInputDtoList.get(i).getIdentityId());
            workflowTaskInput.setKeyColumn(dataInputDtoList.get(i).getKeyColumn());
            workflowTaskInput.setDependentVariable(dataInputDtoList.get(i).getDependentVariable());
            workflowTaskInput.setDataColumnIds(dataInputDtoList.get(i).getDataColumnIds());
            workflowTaskInput.setSortKey(i);
            workflowTaskInput.setPartyId("data" + (i + 1));
            workflowTaskInputList.add(workflowTaskInput);
        }
        return workflowTaskInputList;
    }

    private List<WorkflowTaskOutput>  convert2WorkflowTaskOutput(Long workflowTaskId, OutputDto outputDto){
        return convert2WorkflowTaskOutput(workflowTaskId, outputDto.getStorePattern(), outputDto.getIdentityId());
    }

    private List<WorkflowTaskOutput> convert2WorkflowTaskOutput(Long workflowTaskId, List<WorkflowTaskInput> workflowTaskInputList, Algorithm algorithm){
        return convert2WorkflowTaskOutput(workflowTaskId, algorithm.getStorePattern(), workflowTaskInputList.stream().map(WorkflowTaskInput::getIdentityId).collect(Collectors.toList()));
    }

    private List<WorkflowTaskOutput>  convert2WorkflowTaskOutput(Long workflowTaskId, Integer storePattern, List<String> identityIdList){
        List<WorkflowTaskOutput> workflowTaskOutputList = new ArrayList<>();
        for (int i = 0; i < identityIdList.size(); i++) {
            WorkflowTaskOutput workflowTaskOutput = new WorkflowTaskOutput();
            workflowTaskOutput.setWorkflowTaskId(workflowTaskId);
            workflowTaskOutput.setIdentityId(identityIdList.get(i));
            workflowTaskOutput.setStorePattern(storePattern);
            workflowTaskOutput.setSortKey(i);
            workflowTaskOutput.setPartyId("result" + (i + 1));
            workflowTaskOutputList.add(workflowTaskOutput);
        }
        return workflowTaskOutputList;
    }
}


