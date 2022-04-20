package com.moirae.rosettaflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.protobuf.ByteString;
import com.moirae.rosettaflow.common.enums.ErrorMsg;
import com.moirae.rosettaflow.common.enums.OldAndNewEnum;
import com.moirae.rosettaflow.common.enums.RespCodeEnum;
import com.moirae.rosettaflow.common.enums.WorkflowPayTypeEnum;
import com.moirae.rosettaflow.common.exception.BusinessException;
import com.moirae.rosettaflow.common.utils.LanguageContext;
import com.moirae.rosettaflow.grpc.client.GrpcSysServiceClient;
import com.moirae.rosettaflow.grpc.client.GrpcTaskServiceClient;
import com.moirae.rosettaflow.grpc.constant.GrpcConstant;
import com.moirae.rosettaflow.grpc.service.*;
import com.moirae.rosettaflow.grpc.service.types.SimpleResponse;
import com.moirae.rosettaflow.grpc.service.types.TaskOrganization;
import com.moirae.rosettaflow.grpc.service.types.TaskResourceCostDeclare;
import com.moirae.rosettaflow.grpc.service.types.UserType;
import com.moirae.rosettaflow.manager.*;
import com.moirae.rosettaflow.mapper.domain.*;
import com.moirae.rosettaflow.mapper.enums.CalculationProcessTypeEnum;
import com.moirae.rosettaflow.mapper.enums.TaskStatusEnum;
import com.moirae.rosettaflow.mapper.enums.WorkflowCreateModeEnum;
import com.moirae.rosettaflow.mapper.enums.WorkflowTaskRunStatusEnum;
import com.moirae.rosettaflow.service.*;
import com.moirae.rosettaflow.service.dto.model.ModelDto;
import com.moirae.rosettaflow.service.dto.org.OrgNameDto;
import com.moirae.rosettaflow.service.dto.task.TaskEventDto;
import com.moirae.rosettaflow.service.dto.task.TaskResultDto;
import com.moirae.rosettaflow.service.dto.token.TokenDto;
import com.moirae.rosettaflow.service.dto.token.TokenHolderDto;
import com.moirae.rosettaflow.service.dto.workflow.*;
import com.moirae.rosettaflow.service.dto.workflow.common.*;
import com.moirae.rosettaflow.service.dto.workflow.expert.*;
import com.moirae.rosettaflow.service.dto.workflow.wizard.*;
import com.moirae.rosettaflow.service.utils.CommonUtils;
import com.moirae.rosettaflow.service.utils.TreeUtils;
import com.moirae.rosettaflow.service.utils.UserContext;
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
    private WorkflowTaskCodeManager workflowTaskCodeManager;
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
    private WorkflowRunTaskStatusManager workflowRunTaskStatusManager;
    @Resource
    private WorkflowRunTaskResultManager workflowRunTaskResultManager;

    @Override
    public int getWorkflowCount() {
        return workflowManager.getWorkflowCount(UserContext.getCurrentUser().getAddress());
    }

    @Override
    public IPage<Workflow> getWorkflowList(Long current, Long size, String keyword, Long algorithmId, Date begin, Date end) {
        Page<Workflow> page = new Page<>(current, size);
        workflowManager.getWorkflowList(page, UserContext.getCurrentUser().getAddress(), keyword, algorithmId, begin, end);

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
        return workflowVersionManager.getWorkflowVersionList(page, workflowId);
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
        WorkflowVersionKeyDto result = new WorkflowVersionKeyDto();
        // 配置查询
        AlgorithmClassify rootTree = algService.getAlgTree(true, 1L);
        AlgorithmClassify selectedTree = TreeUtils.findSubTree(rootTree, algorithmId);
        CalculationProcess calculationProcess = getCalculationProcessDetails(calculationProcessId);

        // 创建工作流记录
        Workflow workflow = new Workflow();
        workflow.setCreateMode(WorkflowCreateModeEnum.WIZARD_MODE);
        workflow.setWorkflowName(workflowName);
        workflow.setWorkflowDesc(workflowDesc);
        workflow.setAlgorithmId(algorithmId);
        workflow.setAlgorithmName(selectedTree.getName());
        workflow.setCalculationProcessId(calculationProcessId);
        workflow.setCalculationProcessName(LanguageContext.getByLanguage(calculationProcess.getName(), calculationProcess.getNameEn()));
        workflow.setCalculationProcessStep(0);
        workflow.setWorkflowVersion(1L);
        workflow.setAddress(UserContext.getCurrentUser().getAddress());
        workflowManager.save(workflow);

        // 创建工作流版本
        WorkflowVersion workflowVersion = workflowVersionManager.create(workflow.getWorkflowId(), workflow.getWorkflowVersion(), StringUtils.join(workflowName, "-v1"));

        // 创建工作流任务配置 1-训练  2-预测  3-训练，并预测 4-PSI
        List<WorkflowTask> workflowTaskList = calculationProcess.getTaskItem().stream().map(
                item -> {
                    WorkflowTask workflowTask = new WorkflowTask();
                    workflowTask.setWorkflowId(workflowVersion.getWorkflowId());
                    workflowTask.setWorkflowVersion(workflowVersion.getWorkflowVersion());
                    workflowTask.setStep(item.getStep());
                    Algorithm algorithm = getAlg(item, rootTree, selectedTree);
                    workflowTask.setAlgorithmId(algorithm.getAlgorithmId());
                    workflowTask.setInputModel(algorithm.getInputModel());
                    if(algorithm.getSupportDefaultPsi()){
                        workflowTask.setInputPsi(true);
                    }
                    workflowTaskManager.save(workflowTask);
                    initWorkflowTaskCode(workflowTask.getWorkflowTaskId(), algorithm);
                    initWorkflowTaskVariable(workflowTask.getWorkflowTaskId(), algorithm);
                    initWorkflowTaskResource(workflowTask.getWorkflowTaskId(), algorithm);
                    return workflowTask;
                }
        ).collect(Collectors.toList());

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
        WorkflowVersionKeyDto result = new WorkflowVersionKeyDto();
        // 创建工作流记录
        Workflow workflow = new Workflow();
        workflow.setCreateMode(WorkflowCreateModeEnum.EXPERT_MODE);
        workflow.setWorkflowName(workflowName);
        workflow.setWorkflowVersion(1L);
        workflow.setAddress(UserContext.getCurrentUser().getAddress());
        workflowManager.save(workflow);
        // 创建工作流版本
        workflowVersionManager.create(workflow.getWorkflowId(), workflow.getWorkflowVersion(), StringUtils.join(workflowName, "-v1"));
        result.setWorkflowId(workflow.getWorkflowId());
        result.setWorkflowVersion(workflow.getWorkflowVersion());
        return result;
    }

    @Override
    public List<WorkflowRunTaskStatus> listWorkflowRunTaskStatusOfUnConfirmed() {
        return  workflowRunTaskStatusManager.listOfUnConfirmed();
    }

    @Override
    @Transactional
    public boolean cancelWorkflowRunTaskStatus(WorkflowRunTaskStatus workflowRunTaskStatus) {
        WorkflowRunStatus workflowRunStatus = workflowRunStatusManager.getById(workflowRunTaskStatus.getWorkflowRunId());
        if(workflowRunStatus.getCancelStatus() != null && workflowRunStatus.getCancelStatus() == WorkflowTaskRunStatusEnum.RUN_NEED){
            TerminateTaskRequest request = TerminateTaskRequest.newBuilder()
                    .setUser(workflowRunStatus.getAddress())
                    .setUserType(UserType.User_1)
                    .setTaskId(workflowRunTaskStatus.getTaskId())
                    .setSign(ByteString.copyFromUtf8(workflowRunStatus.getSign()))
                    .build();

            try {
                SimpleResponse response = grpcTaskServiceClient.terminateTask(orgService.getChannel(workflowTaskManager.getById(workflowRunTaskStatus.getWorkflowTaskId()).getIdentityId()), request);
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
    public void taskFinish(WorkflowRunTaskStatus workflowRunTaskStatus, Task task) {
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
                            .map(WorkflowRunTaskStatus::getStep)
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
        WorkflowRunTaskStatus curWorkflowRunTaskStatus = workflowRunStatus.getWorkflowRunTaskStatusList().stream().collect(Collectors.toMap(WorkflowRunTaskStatus::getStep, item -> item)).get(workflowRunStatus.getCurStep());

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
        WorkflowRunTaskStatus curWorkflowRunTaskStatus = workflowRunStatus.getWorkflowRunTaskStatusList().stream().collect(Collectors.toMap(WorkflowRunTaskStatus::getStep, item -> item)).get(workflowRunStatus.getCurStep());
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
        Algorithm algorithm = algService.getAlg(workflowTask.getAlgorithmId(), false);

        List<WorkflowRunTaskResult> taskResultList = new ArrayList<>();
        List<Model> modelList = new ArrayList<>();
        List<Psi> psiList = new ArrayList<>();
        for (String identityId : identityIdSet) {
            GetTaskResultFileSummary response = grpcSysServiceClient.getTaskResultFileSummary(orgService.getChannel(identityId), task.getId());
            if (Objects.isNull(response)) {
                log.error("WorkflowNodeStatusMockTask获取任务结果失败！ info = {}", response);
                return;
            }
            // 处理结果
            WorkflowRunTaskResult taskResult = new WorkflowRunTaskResult();
            taskResult.setIdentityId(identityId);
            taskResult.setTaskId(task.getId());
            taskResult.setFileName(response.getMetadataName());
            taskResult.setMetadataId(response.getMetadataId());
            taskResult.setOriginId(response.getOriginId());
            taskResult.setFilePath(response.getDataPath());
            taskResult.setIp(response.getIp());
            taskResult.setPort(response.getPort());
            taskResultList.add(taskResult);
            // 处理模型
            if(algorithm.getOutputModel()){
                Algorithm predictionAlgorithm = algService.getAlgOfRelativelyPrediction(workflowTask.getAlgorithmId(), false);
                Model model = new Model();
                model.setMetaDataId(taskResult.getMetadataId());
                model.setIdentityId(identityId);
                model.setName(algorithm.getAlgorithmName()+"(" + task.getId() + ")");
                model.setFileId(taskResult.getOriginId());
                model.setFilePath(taskResult.getFilePath());
                model.setTrainTaskId(task.getId());
                model.setTrainAlgorithmId(workflowTask.getAlgorithmId());
                model.setTrainUserAddress(workflowRunStatus.getAddress());
                model.setSupportedAlgorithmId(predictionAlgorithm.getAlgorithmId());
                model.setEvaluate(response.getExtra());
                modelList.add(model);
            }

            if(algorithm.getOutputPsi()){
                Psi psi = new Psi();
                psi.setMetaDataId(taskResult.getMetadataId());
                psi.setIdentityId(identityId);
                psi.setName(algorithm.getAlgorithmName()+"(" + task.getId() + ")");
                psi.setFileId(taskResult.getOriginId());
                psi.setFilePath(taskResult.getFilePath());
                psi.setTrainTaskId(task.getId());
                psi.setTrainAlgorithmId(workflowTask.getAlgorithmId());
                psi.setTrainUserAddress(workflowRunStatus.getAddress());
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


    private void initWorkflowTaskResource(Long workflowTaskId, Algorithm algorithm) {
        WorkflowTaskResource workflowTaskResource = new WorkflowTaskResource();
        workflowTaskResource.setWorkflowTaskId(workflowTaskId);
        workflowTaskResource.setCostCpu(algorithm.getCostCpu());
        workflowTaskResource.setCostGpu(algorithm.getCostGpu());
        workflowTaskResource.setCostMem(algorithm.getCostMem());
        workflowTaskResource.setCostBandwidth(algorithm.getCostBandwidth());
        workflowTaskResource.setRunTime(algorithm.getRunTime());
        workflowTaskResourceManager.save(workflowTaskResource);
    }

    private void initWorkflowTaskVariable(Long workflowTaskId, Algorithm algorithm) {
        if(algorithm.getAlgorithmVariableList().size() == 0){
            return;
        }

        List<WorkflowTaskVariable> workflowTaskVariableList = algorithm.getAlgorithmVariableList().stream()
                .map(item -> {
                    WorkflowTaskVariable workflowTaskVariable = new WorkflowTaskVariable();
                    workflowTaskVariable.setWorkflowTaskId(workflowTaskId);
                    workflowTaskVariable.setVarKey(item.getVarKey());
                    workflowTaskVariable.setVarValue(item.getVarValue());
                    workflowTaskVariable.setVarDesc(item.getVarDesc());
                    workflowTaskVariable.setVarDescEn(item.getVarDescEn());
                    return workflowTaskVariable;
                })
                .collect(Collectors.toList());
        workflowTaskVariableManager.saveBatch(workflowTaskVariableList);
    }

    private void initWorkflowTaskCode(Long workflowTaskId, Algorithm algorithm) {
        if(algorithm.getAlgorithmCode() == null){
            return;
        }
        WorkflowTaskCode workflowTaskCode = new WorkflowTaskCode();
        workflowTaskCode.setWorkflowTaskId(workflowTaskId);
        workflowTaskCode.setEditType(algorithm.getAlgorithmCode().getEditType());
        workflowTaskCode.setCalculateContractStruct(algorithm.getAlgorithmCode().getCalculateContractStruct());
        workflowTaskCode.setCalculateContractCode(algorithm.getAlgorithmCode().getCalculateContractCode());
        workflowTaskCode.setDataSplitContractCode(algorithm.getAlgorithmCode().getDataSplitContractCode());
        workflowTaskCodeManager.save(workflowTaskCode);
    }

    private Algorithm getAlg(CalculationProcessTask calculationProcessTask, AlgorithmClassify rootTree, AlgorithmClassify selectedTree) {
        switch(calculationProcessTask.getAlgorithmSelect()){
            case USER_TRAIN_ALG:
                return selectedTree.getChildrenList().stream().filter(item -> item.getAlg().getOutputModel()).findFirst().get().getAlg();
            case USER_PREDICT_ALG:
                return selectedTree.getChildrenList().stream().filter(item -> item.getAlg().getInputModel()).findFirst().get().getAlg();
            case BUILD_IN_ALG:
                return TreeUtils.findSubTree(rootTree, calculationProcessTask.getBuiltInAlgorithmId()).getAlg();
            default:
                return selectedTree.getAlg();
        }
    }

    private CalculationProcess getCalculationProcessDetails(Long calculationProcessId) {
        CalculationProcess calculationProcess = calculationProcessManager.getById(calculationProcessId);
        calculationProcess.setStepItem(calculationProcessStepManager.getList(calculationProcessId));
        calculationProcess.setTaskItem(calculationProcessTaskManager.getList(calculationProcessId));
        return calculationProcess;
    }

    @Override
    public WorkflowVersionKeyDto settingWorkflowOfWizardMode(WorkflowDetailsOfWizardModeDto req) {
        WorkflowVersionKeyDto result = new WorkflowVersionKeyDto();
        result.setWorkflowId(req.getWorkflowId());
        result.setWorkflowVersion(req.getWorkflowVersion());
        Integer step = req.getCalculationProcessStep().getStep();

        // 更新工作流设置版本
        Workflow workflow = workflowManager.getById(req.getWorkflowId());
        workflowManager.updateStep(workflow.getWorkflowId(), step, calculationProcessStepManager.isEnd(workflow.getCalculationProcessId(), step));

        // 设置参数
        WorkflowSettingWizard wizard = workflowSettingWizardManager.getOneByStep(req.getWorkflowId(), req.getWorkflowVersion(), step);

        switch(req.getCalculationProcessStep().getType()){
            case INPUT_TRAINING:
                setTrainingInputOfWizardMode(req.getWorkflowId(), req.getWorkflowVersion(), wizard.getTask1Step(), wizard.getTask2Step(), req.getTrainingInput());
                break;
            case INPUT_PREDICTION:
                setPredictionInputOfWizardMode(req.getWorkflowId(), req.getWorkflowVersion(), wizard.getTask1Step(), wizard.getTask2Step(), req.getPredictionInput());
                break;
            case INPUT_PSI:
                setTrainingOrPredictionPsiInputOfWizardMode(req.getWorkflowId(),req.getWorkflowVersion(), wizard.getTask2Step(), req.getPsiInput().getIdentityId(), req.getPsiInput().getItem(), false);
                break;
            case RESOURCE_COMMON:
                setCommonResourceOfWizardMode(req.getWorkflowId(), req.getWorkflowVersion(), Arrays.asList(wizard.getTask1Step(), wizard.getTask2Step()), req.getCommonResource());
                break;
            case RESOURCE_TRAINING_PREDICTION:
                setCommonResourceOfWizardMode(req.getWorkflowId(), req.getWorkflowVersion(), Arrays.asList(wizard.getTask1Step(), wizard.getTask2Step()), req.getTrainingAndPredictionResource().getTraining());
                setCommonResourceOfWizardMode(req.getWorkflowId(), req.getWorkflowVersion(), Arrays.asList(wizard.getTask3Step(), wizard.getTask4Step()), req.getTrainingAndPredictionResource().getPrediction());
                break;
            case OUTPUT_COMMON:
                setCommonOutputOfWizardMode(req.getWorkflowId(), req.getWorkflowVersion(), wizard.getTask2Step(), req.getCommonOutput());
                break;
            case OUTPUT_TRAINING_PREDICTION:
                setCommonOutputOfWizardMode(req.getWorkflowId(), req.getWorkflowVersion(), wizard.getTask2Step(), req.getTrainingAndPredictionOutput().getTraining());
                setCommonOutputOfWizardMode(req.getWorkflowId(), req.getWorkflowVersion(), wizard.getTask4Step(), req.getTrainingAndPredictionOutput().getPrediction());
                break;
        }
        return result;
    }



    @Override
    public WorkflowDetailsOfWizardModeDto getWorkflowSettingOfWizardMode(WorkflowWizardStepDto req) {
        WorkflowDetailsOfWizardModeDto result = new WorkflowDetailsOfWizardModeDto();
        WorkflowSettingWizard wizard = workflowSettingWizardManager.getOneByStep(req.getWorkflowId(), req.getWorkflowVersion(), req.getStep());

        result.setWorkflowId(req.getWorkflowId());
        result.setWorkflowVersion(req.getWorkflowVersion());
        CalculationProcessStepDto calculationProcessStepDto = new CalculationProcessStepDto();
        calculationProcessStepDto.setStep(wizard.getStep());
        calculationProcessStepDto.setType(wizard.getCalculationProcessStepType());
        result.setCalculationProcessStep(calculationProcessStepDto);
        switch(wizard.getCalculationProcessStepType()){
            case INPUT_TRAINING:
                result.setTrainingInput(getTrainingInputOfWizardMode(req.getWorkflowId(), req.getWorkflowVersion(), wizard.getTask2Step()));
                break;
            case INPUT_PREDICTION:
                result.setPredictionInput(getPredictionInputOfWizardMode(req.getWorkflowId(), req.getWorkflowVersion(), wizard.getTask2Step()));
                break;
            case INPUT_PSI:
                result.setPsiInput(getPsiInputOfWizardMode(req.getWorkflowId(), req.getWorkflowVersion(), wizard.getTask2Step()));
                break;
            case RESOURCE_COMMON:
                result.setCommonResource(getCommonResourceOfWizardMode(req.getWorkflowId(), req.getWorkflowVersion(), wizard.getTask2Step()));
                break;
            case RESOURCE_TRAINING_PREDICTION:
                result.setTrainingAndPredictionResource(getTrainingAndPredictionResourceOfWizardMode(req.getWorkflowId(), req.getWorkflowVersion(), wizard.getTask1Step(), wizard.getTask2Step()));
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
            outputDto.setStorePattern(algService.getAlg(workflowTask.getAlgorithmId(), false).getStorePattern());
        }
        outputDto.setIdentityId(workflowTaskOutputList.stream().map(WorkflowTaskOutput::getIdentityId).collect(Collectors.toList()));
        return outputDto;
    }

    private void setNodeOutput(Long workflowTaskId, OutputDto nodeOutput) {
        List<WorkflowTaskOutput> workflowTaskOutputList = new ArrayList<>();
        for (int i = 0; i < nodeOutput.getIdentityId().size(); i++) {
            WorkflowTaskOutput workflowTaskOutput = new WorkflowTaskOutput();
            workflowTaskOutput.setWorkflowTaskId(workflowTaskId);
            workflowTaskOutput.setIdentityId(nodeOutput.getIdentityId().get(i));
            workflowTaskOutput.setStorePattern(nodeOutput.getStorePattern());
            workflowTaskOutput.setPartyId("q" + i);
            workflowTaskOutputList.add(workflowTaskOutput);
        }
        workflowTaskOutputManager.saveBatch(workflowTaskOutputList);
    }

    private void setCommonOutputOfWizardMode(Long workflowId, Long workflowVersion, Integer taskStep, OutputDto commonOutput) {
        WorkflowTask workflowTask = workflowTaskManager.getByStep(workflowId, workflowVersion, taskStep);
        List<WorkflowTaskOutput> workflowTaskOutputList = new ArrayList<>();

        for (int i = 0; i < commonOutput.getIdentityId().size(); i++) {
            WorkflowTaskOutput workflowTaskOutput = new WorkflowTaskOutput();
            workflowTaskOutput.setWorkflowTaskId(workflowTask.getWorkflowTaskId());
            workflowTaskOutput.setIdentityId(commonOutput.getIdentityId().get(i));
            workflowTaskOutput.setStorePattern(commonOutput.getStorePattern());
            workflowTaskOutput.setPartyId("q" + i);
            workflowTaskOutputList.add(workflowTaskOutput);
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


    private void setNodeResource(Long workflowTaskId, ResourceDto resource, Boolean supportDefaultPsi, Long psiWorkflowTaskId) {
        List<WorkflowTaskResource> workflowTaskResourceList = new ArrayList<>();
        List<Long> taskIdList = new ArrayList<>();

        WorkflowTaskResource workflowTaskResource = new WorkflowTaskResource();
        workflowTaskResource.setWorkflowTaskId(workflowTaskId);
        workflowTaskResource.setCostCpu(resource.getCostCpu());
        workflowTaskResource.setCostGpu(resource.getCostGpu());
        workflowTaskResource.setCostMem(CommonUtils.convert2DbOfCostMem(resource.getCostMem()));
        workflowTaskResource.setCostBandwidth(CommonUtils.convert2DbOfCostBandwidth(resource.getCostBandwidth()));
        workflowTaskResource.setRunTime(CommonUtils.convert2DbOfRunTime(resource.getRunTime()));
        taskIdList.add(workflowTaskId);
        workflowTaskResourceList.add(workflowTaskResource);

        if(supportDefaultPsi){
            WorkflowTaskResource psiWorkflowTaskResource = new WorkflowTaskResource();
            psiWorkflowTaskResource.setWorkflowTaskId(psiWorkflowTaskId);
            psiWorkflowTaskResource.setCostCpu(resource.getCostCpu());
            psiWorkflowTaskResource.setCostGpu(resource.getCostGpu());
            psiWorkflowTaskResource.setCostMem(CommonUtils.convert2DbOfCostMem(resource.getCostMem()));
            psiWorkflowTaskResource.setCostBandwidth(CommonUtils.convert2DbOfCostBandwidth(resource.getCostBandwidth()));
            psiWorkflowTaskResource.setRunTime(CommonUtils.convert2DbOfRunTime(resource.getRunTime()));
            taskIdList.add(psiWorkflowTaskId);
            workflowTaskResourceList.add(psiWorkflowTaskResource);
        }
        workflowTaskResourceManager.saveBatch(workflowTaskResourceList);
    }




    private void setCommonResourceOfWizardMode(Long workflowId, Long workflowVersion, List<Integer> taskStepList, ResourceDto commonResource) {
        List<WorkflowTaskResource> workflowTaskResourceList = new ArrayList<>();
        List<Long> taskIdList = new ArrayList<>();
        for (Integer taskStep: taskStepList) {
            // 如果是单独psi，其中一个task为null
            if(taskStep == null){
                continue;
            }
            WorkflowTask resource = workflowTaskManager.getByStep(workflowId, workflowVersion, taskStep);
            WorkflowTaskResource workflowTaskResource = new WorkflowTaskResource();
            workflowTaskResource.setWorkflowTaskId(resource.getWorkflowTaskId());
            workflowTaskResource.setCostCpu(commonResource.getCostCpu());
            workflowTaskResource.setCostGpu(commonResource.getCostGpu());
            workflowTaskResource.setCostMem(CommonUtils.convert2DbOfCostMem(commonResource.getCostMem()));
            workflowTaskResource.setCostBandwidth(CommonUtils.convert2DbOfCostBandwidth(commonResource.getCostBandwidth()));
            workflowTaskResource.setRunTime(CommonUtils.convert2DbOfRunTime(commonResource.getRunTime()));

            taskIdList.add(resource.getWorkflowTaskId());
            workflowTaskResourceList.add(workflowTaskResource);
        }
        workflowTaskResourceManager.clearAndSave(taskIdList, workflowTaskResourceList);
    }

    private PsiInputDto getPsiInputOfWizardMode(Long workflowId, Long workflowVersion, Integer task1Step) {
        PsiInputDto psiInputDto = new PsiInputDto();
        WorkflowTask psi = workflowTaskManager.getByStep(workflowId, workflowVersion, task1Step);
        psiInputDto.setIdentityId(psi.getIdentityId());
        psiInputDto.setItem(BeanUtil.copyToList(workflowTaskInputManager.listByWorkflowTaskId(psi.getWorkflowTaskId()), DataInputDto.class));
        return psiInputDto;
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

    private void setPredictionInputOfWizardMode(Long workflowId, Long workflowVersion, Integer psiTaskStep, Integer task2Step, PredictionInputDto predictionInput) {
        WorkflowTask prediction = workflowTaskManager.getByStep(workflowId, workflowVersion, task2Step);
        prediction.setIdentityId(predictionInput.getIdentityId());
        prediction.setInputPsi(predictionInput.getIsPsi());
        if(predictionInput.getModel()!=null){
            prediction.setInputModelId(predictionInput.getModel().getMetaDataId());
        }
        workflowTaskManager.updateById(prediction);

        List<WorkflowTaskInput> predictionWorkflowTaskInputList = new ArrayList<>();
        for (int i = 0; i < predictionInput.getItem().size(); i++) {
            WorkflowTaskInput workflowTaskInput = new WorkflowTaskInput();
            workflowTaskInput.setWorkflowTaskId(prediction.getWorkflowTaskId());
            workflowTaskInput.setPartyId("p" + i);
            BeanUtil.copyProperties(predictionInput.getItem().get(i), workflowTaskInput);
            predictionWorkflowTaskInputList.add(workflowTaskInput);
        }

        workflowTaskInputManager.clearAndSave(prediction.getWorkflowTaskId(), predictionWorkflowTaskInputList);

        WorkflowTask psi = workflowTaskManager.getByStep(workflowId, workflowVersion, psiTaskStep);
        if(psi.getEnable() != predictionInput.getIsPsi()){
            psi.setEnable(predictionInput.getIsPsi());
            workflowTaskManager.updateById(psi);
        }

        if(predictionInput.getIsPsi()){
            setTrainingOrPredictionPsiInputOfWizardMode(workflowId, workflowVersion, psiTaskStep, predictionInput.getIdentityId(), predictionInput.getItem(), true);
        }
    }


    private TrainingInputDto getTrainingInputOfWizardMode(Long workflowId, Long workflowVersion, Integer task2Step) {
        TrainingInputDto trainingInputDto = new TrainingInputDto();
        WorkflowTask training = workflowTaskManager.getByStep(workflowId, workflowVersion, task2Step);
        trainingInputDto.setIdentityId(training.getIdentityId());
        trainingInputDto.setIsPsi(training.getInputPsi());
        trainingInputDto.setItem(BeanUtil.copyToList(workflowTaskInputManager.listByWorkflowTaskId(training.getWorkflowTaskId()), DataInputDto.class));
        return trainingInputDto;
    }

    private void setTrainingInputOfWizardMode(Long workflowId, Long workflowVersion, Integer psiTaskStep, Integer task2Step, TrainingInputDto trainingInput) {
        WorkflowTask training = workflowTaskManager.getByStep(workflowId, workflowVersion, task2Step);
        training.setIdentityId(trainingInput.getIdentityId());
        training.setInputPsi(trainingInput.getIsPsi());
        workflowTaskManager.updateById(training);

        List<WorkflowTaskInput> trainingWorkflowTaskInputList = new ArrayList<>();
        for (int i = 0; i < trainingInput.getItem().size(); i++) {
            WorkflowTaskInput workflowTaskInput = new WorkflowTaskInput();
            workflowTaskInput.setWorkflowTaskId(training.getWorkflowTaskId());
            workflowTaskInput.setPartyId("p" + i);
            BeanUtil.copyProperties(trainingInput.getItem().get(i), workflowTaskInput);
            trainingWorkflowTaskInputList.add(workflowTaskInput);
        }
        workflowTaskInputManager.clearAndSave(training.getWorkflowTaskId(), trainingWorkflowTaskInputList);

        WorkflowTask psi = workflowTaskManager.getByStep(workflowId, workflowVersion, psiTaskStep);
        if(psi.getEnable() != trainingInput.getIsPsi()){
            psi.setEnable(trainingInput.getIsPsi());
            workflowTaskManager.updateById(psi);
        }

        if(trainingInput.getIsPsi()){
            setTrainingOrPredictionPsiInputOfWizardMode(workflowId, workflowVersion, psiTaskStep, trainingInput.getIdentityId(), trainingInput.getItem(), true);
        }
    }

    private void setNodeInput(Long workflowTaskId, List<DataInputDto> dataInputList, boolean hasPis, Long psiWorkflowTaskId, Algorithm psiAlg) {
        List<WorkflowTaskInput> workflowTaskInputList = new ArrayList<>();
        for (int i = 0; i < dataInputList.size(); i++) {
            WorkflowTaskInput workflowTaskInput = new WorkflowTaskInput();
            workflowTaskInput.setWorkflowTaskId(workflowTaskId);
            workflowTaskInput.setPartyId("p" + i);
            BeanUtil.copyProperties(dataInputList.get(i), workflowTaskInput);
            workflowTaskInputList.add(workflowTaskInput);
        }
        workflowTaskInputManager.saveBatch(workflowTaskInputList);

        if(hasPis){
            // 设置psi的输入
            List<WorkflowTaskInput> psiWorkflowTaskInputList = new ArrayList<>();
            for (int i = 0; i < dataInputList.size(); i++) {
                WorkflowTaskInput workflowTaskInput = new WorkflowTaskInput();
                workflowTaskInput.setWorkflowTaskId(psiWorkflowTaskId);
                workflowTaskInput.setMetaDataId(dataInputList.get(i).getMetaDataId());
                workflowTaskInput.setIdentityId(dataInputList.get(i).getIdentityId());
                workflowTaskInput.setKeyColumn(dataInputList.get(i).getKeyColumn());
                workflowTaskInput.setPartyId("p" + i);
                psiWorkflowTaskInputList.add(workflowTaskInput);
            }
            workflowTaskInputManager.saveBatch(psiWorkflowTaskInputList);

            // 设置psi的输出
            List<WorkflowTaskOutput> workflowTaskOutputList = new ArrayList<>();
            for (int i = 0; i < dataInputList.size(); i++) {
                WorkflowTaskOutput workflowTaskOutput = new WorkflowTaskOutput();
                workflowTaskOutput.setWorkflowTaskId(psiWorkflowTaskId);
                workflowTaskOutput.setIdentityId(dataInputList.get(i).getIdentityId());
                workflowTaskOutput.setStorePattern(psiAlg.getStorePattern());
                workflowTaskOutput.setPartyId("q" + i);
                workflowTaskOutputList.add(workflowTaskOutput);
            }
            workflowTaskOutputManager.saveBatch(workflowTaskOutputList);
        }
    }

    private void setTrainingOrPredictionPsiInputOfWizardMode(Long workflowId, Long workflowVersion, Integer psiTaskStep, String senderIdentityId, List<DataInputDto> item, boolean setOut){
        WorkflowTask psi = workflowTaskManager.getByStep(workflowId, workflowVersion, psiTaskStep);
        psi.setIdentityId(senderIdentityId);
        workflowTaskManager.updateById(psi);

        List<WorkflowTaskInput> psiWorkflowTaskInputList = new ArrayList<>();
        for (int i = 0; i < item.size(); i++) {
            WorkflowTaskInput workflowTaskInput = new WorkflowTaskInput();
            workflowTaskInput.setWorkflowTaskId(psi.getWorkflowTaskId());
            workflowTaskInput.setMetaDataId(item.get(i).getMetaDataId());
            workflowTaskInput.setIdentityId(item.get(i).getIdentityId());
            workflowTaskInput.setKeyColumn(item.get(i).getKeyColumn());
            workflowTaskInput.setPartyId("p" + i);
            psiWorkflowTaskInputList.add(workflowTaskInput);
        }
        workflowTaskInputManager.clearAndSave(psi.getWorkflowTaskId(), psiWorkflowTaskInputList);
        if(setOut){
            setPsiOutputOfWizardMode(psi.getWorkflowTaskId(), item, psi.getAlgorithmId());
        }
    }

    private void setPsiOutputOfWizardMode(Long workflowTaskId, List<DataInputDto> inputDtoList, Long algorithmId) {
        List<WorkflowTaskOutput> workflowTaskOutputList = new ArrayList<>();
        for (int i = 0; i < inputDtoList.size(); i++) {
            WorkflowTaskOutput workflowTaskOutput = new WorkflowTaskOutput();
            workflowTaskOutput.setWorkflowTaskId(workflowTaskId);
            workflowTaskOutput.setIdentityId(inputDtoList.get(i).getIdentityId());
            workflowTaskOutput.setStorePattern(algService.getAlg(algorithmId, false).getStorePattern());
            workflowTaskOutput.setPartyId("q" + i);
            workflowTaskOutputList.add(workflowTaskOutput);
        }
        workflowTaskOutputManager.clearAndSave(workflowTaskId, workflowTaskOutputList);
    }

    @Override
    @Transactional
    public WorkflowVersionKeyDto settingWorkflowOfExpertMode(WorkflowDetailsOfExpertModeDto req) {
        // 清理节点设置
        clearWorkflowOfExpertMode(req.getWorkflowId(), req.getWorkflowVersion());
        // 创建工作流任务配置
        List<WorkflowSettingExpert> workflowSettingExpertList = new ArrayList<>();
        int taskStep = 1;
        for (int i = 0; i < req.getWorkflowNodeList().size(); i++) {
            NodeDto nodeDto = req.getWorkflowNodeList().get(i);
            WorkflowSettingExpert workflowSettingExpert = new WorkflowSettingExpert();
            workflowSettingExpert.setWorkflowId(req.getWorkflowId());
            workflowSettingExpert.setWorkflowVersion(req.getWorkflowVersion());
            workflowSettingExpert.setNodeStep(nodeDto.getNodeStep());
            workflowSettingExpert.setNodeName(nodeDto.getNodeName());

            WorkflowTask psiWorkflowTask = new WorkflowTask();
            psiWorkflowTask.setWorkflowId(req.getWorkflowId());
            psiWorkflowTask.setWorkflowVersion(req.getWorkflowVersion());
            psiWorkflowTask.setStep(taskStep++);
            psiWorkflowTask.setAlgorithmId(1001L);
            psiWorkflowTask.setIdentityId(nodeDto.getNodeInput().getIdentityId());
            psiWorkflowTask.setEnable(nodeDto.getNodeInput().getIsPsi());
            workflowTaskManager.save(psiWorkflowTask);
            Algorithm psiAlgorithm = algService.getAlg(psiWorkflowTask.getAlgorithmId(),true);
            initWorkflowTaskCode(psiWorkflowTask.getWorkflowTaskId(), psiAlgorithm);
            initWorkflowTaskVariable(psiWorkflowTask.getWorkflowTaskId(), psiAlgorithm);

            WorkflowTask workflowTask = new WorkflowTask();
            workflowTask.setWorkflowId(req.getWorkflowId());
            workflowTask.setWorkflowVersion(req.getWorkflowVersion());
            workflowTask.setStep(taskStep++);
            workflowTask.setAlgorithmId(nodeDto.getAlgorithmId());
            Algorithm algorithm = algService.getAlg(workflowTask.getAlgorithmId(), false);
            workflowTask.setIdentityId(nodeDto.getNodeInput().getIdentityId());
            workflowTask.setInputModel(nodeDto.getNodeInput().getInputModel());
            if(nodeDto.getNodeInput().getInputModel() && nodeDto.getNodeInput().getModel() != null){
                workflowTask.setInputModelId(nodeDto.getNodeInput().getModel().getMetaDataId());
            }
            workflowTask.setInputPsi(nodeDto.getNodeInput().getIsPsi());
            workflowTaskManager.save(workflowTask);

            workflowSettingExpert.setPsiTaskStep(psiWorkflowTask.getStep());
            workflowSettingExpert.setTaskStep(workflowTask.getStep());
            workflowSettingExpertList.add(workflowSettingExpert);

            setNodeInput(workflowTask.getWorkflowTaskId(), nodeDto.getNodeInput().getDataInputList(), algorithm.getSupportDefaultPsi(), psiWorkflowTask.getWorkflowTaskId(), psiAlgorithm);
            setNodeCode(workflowTask.getWorkflowTaskId(), nodeDto.getNodeCode());
            setNodeOutput(workflowTask.getWorkflowTaskId(),  nodeDto.getNodeOutput());
            setNodeResource(workflowTask.getWorkflowTaskId(), nodeDto.getResource(), algorithm.getSupportDefaultPsi(), psiWorkflowTask.getWorkflowTaskId());
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
        WorkflowTaskCode workflowTaskCode = new WorkflowTaskCode();
        workflowTaskCode.setWorkflowTaskId(workflowTaskId);
        workflowTaskCode.setEditType(nodeCode.getCode().getEditType());
        workflowTaskCode.setCalculateContractCode(nodeCode.getCode().getCalculateContractCode());
        workflowTaskCode.setDataSplitContractCode(nodeCode.getCode().getDataSplitContractCode());
        workflowTaskCodeManager.save(workflowTaskCode);

        List<WorkflowTaskVariable> workflowTaskVariableList = nodeCode.getVariableList().stream()
                .map(item -> {
                    WorkflowTaskVariable workflowTaskVariable = new WorkflowTaskVariable();
                    workflowTaskVariable.setWorkflowTaskId(workflowTaskId);
                    workflowTaskVariable.setVarKey(item.getVarKey());
                    workflowTaskVariable.setVarValue(item.getVarValue());
                    workflowTaskVariable.setVarDesc(item.getVarDesc());
                    workflowTaskVariable.setVarDescEn(item.getVarDescEn());
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
            workflowTaskCodeManager.removeByIds(workflowTaskIdList);
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
        result.setWorkflowId(req.getWorkflowId());
        result.setWorkflowVersion(req.getWorkflowVersion());
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
                    nodeDto.setAlgorithmId(workflowTask.getAlgorithmId());
                    NodeCodeDto nodeCodeDto = new NodeCodeDto();
                    nodeCodeDto.setCode(BeanUtil.copyProperties(workflowTaskCodeManager.getById(workflowTask.getWorkflowTaskId()),CodeDto.class));
                    nodeCodeDto.setVariableList(BeanUtil.copyToList(workflowTaskVariableManager.listByWorkflowTaskId(workflowTask.getWorkflowTaskId()), VariableDto.class));
                    nodeDto.setNodeCode(nodeCodeDto);
                    NodeInputDto nodeInputDto = new NodeInputDto();
                    nodeInputDto.setIdentityId(workflowTask.getIdentityId());
                    nodeInputDto.setInputModel(workflowTask.getInputModel());
                    if(workflowTask.getInputModel() && StringUtils.isNotBlank(workflowTask.getInputModelId())){
                        nodeInputDto.setModel(BeanUtil.copyProperties(dataService.getModelById(workflowTask.getInputModelId()), ModelDto.class));
                    }
                    nodeInputDto.setIsPsi(workflowTask.getInputPsi());
                    nodeInputDto.setDataInputList(BeanUtil.copyToList(workflowTaskInputManager.listByWorkflowTaskId(workflowTask.getWorkflowTaskId()), DataInputDto.class));
                    nodeDto.setNodeInput(nodeInputDto);
                    nodeDto.setResource(BeanUtil.copyProperties(workflowTaskResourceManager.getById(workflowTask.getWorkflowTaskId()), ResourceDto.class));
                    OutputDto outputDto = new OutputDto();
                    List<WorkflowTaskOutput> workflowTaskOutputList = workflowTaskOutputManager.listByWorkflowTaskId(workflowTask.getWorkflowTaskId());
                    if(workflowTaskOutputList.size() > 0){
                        outputDto.setStorePattern(workflowTaskOutputList.get(0).getStorePattern());
                    }else{
                        outputDto.setStorePattern(algService.getAlg(workflowTask.getAlgorithmId(), false).getStorePattern());
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
           WorkflowRunTaskStatus psiWorkflowRunTaskStatus = workflowRunTaskStatusManager.getByWorkflowRunIdAndStep(id, psiTaskStep);
           if(psiWorkflowRunTaskStatus != null && psiWorkflowRunTaskStatus.getRunStatus() == WorkflowTaskRunStatusEnum.RUN_FAIL){
                return WorkflowTaskRunStatusEnum.RUN_FAIL;
           }
        }
        WorkflowRunTaskStatus workflowRunTaskStatus = workflowRunTaskStatusManager.getByWorkflowRunIdAndStep(id, taskStep);
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
                .map(WorkflowRunTaskStatus::getTaskId)
                .flatMap(item -> taskService.getTaskEventList(item).stream())
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
                .map(WorkflowRunTaskStatus::getTaskId)
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
        WorkflowVersionKeyDto result = new WorkflowVersionKeyDto();
        // 更新工作流对象
        Workflow workflow = workflowManager.increaseVersion(req.getWorkflowId());
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
            workflowTaskCodeManager.copy(item.get(OldAndNewEnum.OLD).getWorkflowTaskId(), item.get(OldAndNewEnum.NEW).getWorkflowTaskId());
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
            workflowTaskCodeManager.deleteByWorkflowTaskId(item.getWorkflowTaskId());
            workflowTaskInputManager.deleteByWorkflowTaskId(item.getWorkflowTaskId());
            workflowTaskOutputManager.deleteByWorkflowTaskId(item.getWorkflowTaskId());
            workflowTaskResourceManager.deleteByWorkflowTaskId(item.getWorkflowTaskId());
            workflowTaskVariableManager.deleteByWorkflowTaskId(item.getWorkflowTaskId());
        });
        return true;
    }

    @Override
    public List<WorkflowFeeDto> preparationStart(WorkflowVersionKeyDto req) {
        List<WorkflowTask> workflowTaskList = listExecutableDetailsByWorkflowVersion(req.getWorkflowId(), req.getWorkflowVersion());
        return convert2WorkflowFee(req.getWorkflowId(), req.getWorkflowVersion(), workflowTaskList);
    }

    @Transactional
    @Override
    public WorkflowRunKeyDto start(WorkflowStartSignatureDto req) {
        // 生成运行时任务清单明细
        List<WorkflowTask> workflowTaskList = listExecutableDetailsByWorkflowVersion(req.getWorkflowId(), req.getWorkflowVersion());
        // 交易-手续费校验
        List<WorkflowFeeDto> workflowFeeList = convert2WorkflowFee(req.getWorkflowId(), req.getWorkflowVersion(), workflowTaskList);
        checkFee(workflowFeeList);
        // 保存运行时信息
        WorkflowRunStatus workflowRunStatus = createAndSaveWorkflowRunStatus(req.getAddress(), req.getSign(), req.getWorkflowId(), req.getWorkflowVersion(), workflowTaskList);
        // 启动任务
        executeTask(workflowRunStatus);
        // 返回
        WorkflowRunKeyDto result = new WorkflowRunKeyDto();
        result.setWorkflowRunId(workflowRunStatus.getId());
        return result;
    }

    private void executeTask(WorkflowRunStatus workflowRunStatus) {
        WorkflowRunTaskStatus curWorkflowRunTaskStatus = workflowRunStatus.getWorkflowRunTaskStatusList().stream().collect(Collectors.toMap(WorkflowRunTaskStatus::getStep, item -> item)).get(workflowRunStatus.getCurStep());

        if(curWorkflowRunTaskStatus.getRunStatus() == WorkflowTaskRunStatusEnum.RUN_NEED){
            // 运行时初始化依赖
            initModelAndPsi(curWorkflowRunTaskStatus);

            curWorkflowRunTaskStatus.setRunStatus(WorkflowTaskRunStatusEnum.RUN_DOING);
            curWorkflowRunTaskStatus.setBeginTime(new Date());
            // 提交任务到 Net
            PublishTaskDeclareRequest request = assemblyTask(workflowRunStatus, curWorkflowRunTaskStatus);
            try {
                PublishTaskDeclareResponse response = grpcTaskServiceClient.publishTaskDeclare(orgService.getChannel(curWorkflowRunTaskStatus.getWorkflowTask().getIdentityId()), request);
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

    private PublishTaskDeclareRequest assemblyTask(WorkflowRunStatus workflowRunStatus, WorkflowRunTaskStatus curWorkflowRunTaskStatus) {
        PublishTaskDeclareRequest.Builder requestBuild = PublishTaskDeclareRequest.newBuilder()
                .setTaskName(publishTaskOfGetTaskName(workflowRunStatus, curWorkflowRunTaskStatus))
                .setUser(workflowRunStatus.getAddress())
                .setUserType(UserType.User_1)
                .setSender(publishTaskOfGetTaskOrganization(curWorkflowRunTaskStatus.getWorkflowTask().getOrg(), "s0"))
                .setAlgoSupplier(publishTaskOfGetTaskOrganization(curWorkflowRunTaskStatus.getWorkflowTask().getOrg(), "A0"));

        // 设置数据输入组织
        JSONArray dataPolicyOption = new JSONArray();
        for (int i = 0; i < curWorkflowRunTaskStatus.getWorkflowTask().getInputList().size(); i++) {
            WorkflowTaskInput workflowTaskInput = curWorkflowRunTaskStatus.getWorkflowTask().getInputList().get(i);
            requestBuild.addDataSuppliers(publishTaskOfGetTaskOrganization(workflowTaskInput.getOrg(), workflowTaskInput.getPartyId()));
            dataPolicyOption.add(createDataPolicyItem(workflowTaskInput));
        }
        // 设置模型输入组织
        if(curWorkflowRunTaskStatus.getWorkflowTask().getInputModel()){
            String partyId = "p" + (requestBuild.getDataSuppliersBuilderList().size() - 1);
            requestBuild.addDataSuppliers(publishTaskOfGetTaskOrganization(curWorkflowRunTaskStatus.getModel().getOrg(), partyId));
            dataPolicyOption.add(createDataPolicyItem(curWorkflowRunTaskStatus.getModel(), partyId));
        }
        // 设置psi输入组织
        if(curWorkflowRunTaskStatus.getWorkflowTask().getInputPsi()){
            for (int i = 0; i < curWorkflowRunTaskStatus.getWorkflowTask().getInputList().size(); i++) {
                WorkflowTaskInput workflowTaskInput = curWorkflowRunTaskStatus.getWorkflowTask().getInputList().get(i);
                Psi psi = curWorkflowRunTaskStatus.getPsiList().stream()
                        .filter(item -> workflowTaskInput.getIdentityId().equals(item.getIdentityId()))
                        .findFirst().get();
                String partyId = "p" + (requestBuild.getDataSuppliersBuilderList().size() - 1);
                requestBuild.addDataSuppliers(publishTaskOfGetTaskOrganization(psi.getOrg(), partyId));
                dataPolicyOption.add(createDataPolicyItem(psi, partyId));
            }
         }

        requestBuild.setDataPolicyType(1);
        requestBuild.setDataPolicyOption(dataPolicyOption.toJSONString());

        for (int i = 0; i < curWorkflowRunTaskStatus.getWorkflowTask().getOutputList().size(); i++) {
            WorkflowTaskOutput workflowTaskOutput = curWorkflowRunTaskStatus.getWorkflowTask().getOutputList().get(i);
            requestBuild.addReceivers(publishTaskOfGetTaskOrganization(workflowTaskOutput.getOrg(), workflowTaskOutput.getPartyId()));
        }

        JSONArray powerPolicyOption = new JSONArray();
        powerPolicyOption.add("y1");
        powerPolicyOption.add("y2");
        powerPolicyOption.add("y3");
        requestBuild.setPowerPolicyType(1);
        requestBuild.setPowerPolicyOption(powerPolicyOption.toJSONString());

        // data_flow_policy_type & data_flow_policy_option 设置未定义
        requestBuild.setDataPolicyType(0);
        requestBuild.setDataFlowPolicyOption("");

        WorkflowTaskResource resource = curWorkflowRunTaskStatus.getWorkflowTask().getResource();
        TaskResourceCostDeclare taskResourceCostDeclare = TaskResourceCostDeclare.newBuilder()
                .setMemory(resource.getCostMem())
                .setProcessor(resource.getCostCpu())
                .setBandwidth(resource.getCostBandwidth())
                .setDuration(resource.getRunTime())
                .build();
        requestBuild.setOperationCost(taskResourceCostDeclare);

        requestBuild.setAlgorithmCode(curWorkflowRunTaskStatus.getWorkflowTask().getCode().getCalculateContractCode());
        requestBuild.setMetaAlgorithmId("");
        requestBuild.setAlgorithmCodeExtraParams(createAlgorithmCodeExtraParams(curWorkflowRunTaskStatus.getWorkflowTask().getCode().getCalculateContractStruct(), curWorkflowRunTaskStatus.getWorkflowTask().getVariableList()));


        requestBuild.setSign(ByteString.copyFromUtf8(workflowRunStatus.getSign()));
        requestBuild.setDesc("");
        return requestBuild.build();
    }

    private String createAlgorithmCodeExtraParams(String calculateContractStruct, List<WorkflowTaskVariable> variableList) {
        //TODO

        return "";
    }


    private JSONObject createDataPolicyItem(Psi psi, String partyId) {
        JSONObject item = new JSONObject();
        item.put("partyId", partyId);
        item.put("metadataId", psi.getMetaDataId());
        item.put("metadataName", psi.getName());
        item.put("keyColumn", 0);
        return item;
    }

    private JSONObject createDataPolicyItem(Model model, String partyId) {
        JSONObject item = new JSONObject();
        item.put("partyId", partyId);
        item.put("metadataId", model.getMetaDataId());
        item.put("metadataName", model.getName());
        item.put("keyColumn", 0);
        return item;
    }

    private JSONObject createDataPolicyItem(WorkflowTaskInput workflowTaskInput) {
        JSONObject item = new JSONObject();
        item.put("partyId", workflowTaskInput.getPartyId());
        item.put("metadataId", workflowTaskInput.getMetaDataId());
        item.put("metadataName", dataService.getDataById(workflowTaskInput.getMetaDataId()).getMetaDataName());
        item.put("keyColumn", workflowTaskInput.getKeyColumn());
        JSONArray selectedColumns = new JSONArray();
        Arrays.stream(workflowTaskInput.getDataColumnIds().split(",")).forEach(subItem -> {
            selectedColumns.add(subItem);
        });
        item.put("selectedColumns", selectedColumns);
        return item;
    }

    private TaskOrganization publishTaskOfGetTaskOrganization(Org identity, String partyId){
        TaskOrganization taskOrganization = TaskOrganization.newBuilder()
                .setPartyId(partyId)
                .setNodeName(identity.getNodeName())
                .setNodeId(identity.getNodeId())
                .setIdentityId(identity.getIdentityId())
                .build();
        return taskOrganization;
    }

    private String publishTaskOfGetTaskName(WorkflowRunStatus workflowRunStatus, WorkflowRunTaskStatus curWorkflowRunTaskStatus) {
        Long id = curWorkflowRunTaskStatus.getId();
        String address = workflowRunStatus.getAddress();
        String algorithmName = curWorkflowRunTaskStatus.getWorkflowTask().getAlgorithm().getAlgorithmName();
        String workflowName = workflowRunStatus.getWorkflow().getWorkflowName();
        return StringUtils.abbreviate(id+ "_" + address + "_" + algorithmName + "_" + workflowName, 100);
    }

    private WorkflowRunStatus loadWorkflowRunStatus(WorkflowRunStatus workflowRunStatus, List<WorkflowTask> workflowTaskList) {
        List<WorkflowRunTaskStatus> workflowRunTaskStatusList = workflowRunTaskStatusManager.listByWorkflowRunIdAndHasTaskId(workflowRunStatus.getId());
        Map<Long, WorkflowTask> workflowTaskMap = workflowTaskList.stream().collect(Collectors.toMap(WorkflowTask::getWorkflowTaskId, item -> item));

        workflowRunTaskStatusList.stream().forEach(item -> {
            item.setWorkflowTask(workflowTaskMap.get(item.getWorkflowTaskId()));
        });
        workflowRunStatus.setWorkflowRunTaskStatusList(workflowRunTaskStatusList);
        return workflowRunStatus;
    }

    private WorkflowRunStatus createAndSaveWorkflowRunStatus(String address, String sign, Long workflowId, Long workflowVersion, List<WorkflowTask> workflowTaskList) {
        WorkflowRunStatus workflowRunStatus = new WorkflowRunStatus();
        workflowRunStatus.setWorkflowId(workflowId);
        workflowRunStatus.setWorkflowVersion(workflowVersion);
        workflowRunStatus.setSign(sign);
        workflowRunStatus.setAddress(address);
        workflowRunStatus.setStep(workflowTaskList.get(workflowTaskList.size() - 1).getStep());
        workflowRunStatus.setCurStep(workflowTaskList.get(0).getStep());
        workflowRunStatus.setRunStatus(WorkflowTaskRunStatusEnum.RUN_DOING);
        workflowRunStatusManager.save(workflowRunStatus);

        List<WorkflowRunTaskStatus> workflowRunTaskStatusList = workflowTaskList.stream()
                .map(item -> {
                    WorkflowRunTaskStatus workflowRunTaskStatus = new WorkflowRunTaskStatus();
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
        return workflowRunStatus;
    }


    /**
     * 检验用户账户余额是否足够执行任务
     *
     * @param workflowFeeList
     */
    private void checkFee(List<WorkflowFeeDto> workflowFeeList) {
        long count = workflowFeeList.stream()
                .flatMap(item -> item.getItemList().stream())
                .filter(item -> !item.getIsEnough()).count();
        if( count > 0) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_EXECUTE_VALUE_INSUFFICIENT.getMsg());
        }
    }

    private List<WorkflowFeeDto> convert2WorkflowFee(Long workflowId, Long workflowVersion, List<WorkflowTask> workflowTaskList) {
        return workflowTaskList.stream()
                .map(item -> {
                    List<WorkflowTaskFeeItemDto> workflowTaskFeeItemDtoList = new ArrayList<>();
                    List<DataTokenTransferItem>  dataTokenTransferItemList = new ArrayList<>();
                    for (WorkflowTaskInput workflowTaskInput : item.getInputList()) {
                        WorkflowTaskFeeItemDto workflowTaskFeeItemDto = createWorkflowTaskFeeItemDto(WorkflowPayTypeEnum.TOKEN, "1000000000000000000", dataService.getTokenByMetaDataId(workflowTaskInput.getMetaDataId()));
                        workflowTaskFeeItemDtoList.add(workflowTaskFeeItemDto);
                        DataTokenTransferItem dataTokenTransferItem = DataTokenTransferItem.newBuilder()
                                .setAddress(workflowTaskFeeItemDto.getToken().getAddress())
                                .setAmount(Long.valueOf(workflowTaskFeeItemDto.getNeedValue())).build();
                        dataTokenTransferItemList.add(dataTokenTransferItem);
                    }

                    EstimateTaskGasRequest request = EstimateTaskGasRequest.newBuilder()
                            .addAllDataTokenTransferItems(dataTokenTransferItemList)
                            .build();
                    EstimateTaskGasResponse response = grpcTaskServiceClient.estimateTaskGas(orgService.getChannel(item.getIdentityId()), request);
                    WorkflowTaskFeeItemDto workflowTaskFeeItemDto = createWorkflowTaskFeeItemDto(WorkflowPayTypeEnum.FEE, BigInteger.valueOf(response.getGasLimit()).multiply(BigInteger.valueOf(response.getGasPrice())).toString(), dataService.getMetisToken());
                    workflowTaskFeeItemDtoList.add(workflowTaskFeeItemDto);

                    WorkflowFeeDto workflowFeeDto = new WorkflowFeeDto();
                    workflowFeeDto.setWorkflowId(workflowId);
                    workflowFeeDto.setWorkflowVersion(workflowVersion);
                    workflowFeeDto.setWorkflowTaskId(item.getWorkflowTaskId());
                    workflowFeeDto.setItemList(workflowTaskFeeItemDtoList);
                    return workflowFeeDto;
                })
                .collect(Collectors.toList());
    }

    private WorkflowTaskFeeItemDto createWorkflowTaskFeeItemDto(WorkflowPayTypeEnum typeEnum,  String value, Token token){
        WorkflowTaskFeeItemDto workflowTaskFeeItemDto = new WorkflowTaskFeeItemDto();
        workflowTaskFeeItemDto.setType(typeEnum);
        workflowTaskFeeItemDto.setNeedValue(value);
        workflowTaskFeeItemDto.setToken(BeanUtil.copyProperties(token, TokenDto.class));
        workflowTaskFeeItemDto.setTokenHolder(BeanUtil.copyProperties(dataService.getTokenHolderById(token.getAddress(), UserContext.getCurrentUser().getAddress()), TokenHolderDto.class));
        workflowTaskFeeItemDto.setIsEnough(
                new BigDecimal(workflowTaskFeeItemDto.getTokenHolder().getBalance()).compareTo(new BigDecimal(workflowTaskFeeItemDto.getNeedValue()))>0
                        && new BigDecimal(workflowTaskFeeItemDto.getTokenHolder().getAuthorizeBalance()).compareTo(new BigDecimal(workflowTaskFeeItemDto.getNeedValue())) >= 0);
        return workflowTaskFeeItemDto;
    }


    private void initModelAndPsi(WorkflowRunTaskStatus curWorkflowRunTaskStatus) {
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
            curWorkflowRunTaskStatus.setPsiList(dataService.listPsiByTrainTaskId(workflowRunTaskStatusManager.getByWorkflowRunIdAndStep(curWorkflowRunTaskStatus.getWorkflowRunId(), workflowTask.getInputPsiStep()).getTaskId()));
            // psi的组织
            curWorkflowRunTaskStatus.getPsiList().forEach(item -> {
                item.setOrg(orgService.getOrgById(item.getIdentityId()));
            });
        }
    }

    private List<WorkflowTask> listExecutableDetailsByWorkflowVersion(Long workflowId, Long workflowVersion){
        List<WorkflowTask> workflowTaskList = workflowTaskManager.listExecutableByWorkflowVersion(workflowId, workflowVersion);
        workflowTaskList.stream().forEach(item ->{
            // 设置 code
            item.setCode(workflowTaskCodeManager.getById(item.getWorkflowTaskId()));
            // 设置 input
            item.setInputList(workflowTaskInputManager.listByWorkflowTaskId(item.getWorkflowTaskId()));
            // 设置 output
            item.setOutputList(workflowTaskOutputManager.listByWorkflowTaskId(item.getWorkflowTaskId()));
            // 设置 resource
            item.setResource(workflowTaskResourceManager.getById(item.getWorkflowTaskId()));
            // 设置 variable
            item.setVariableList(workflowTaskVariableManager.listByWorkflowTaskId(item.getWorkflowTaskId()));
            // 设置 算法
            item.setAlgorithm(algService.getAlg(item.getAlgorithmId(), false));
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
        // 校验是否运行中
        if (workflowRunStatus.getRunStatus() != WorkflowTaskRunStatusEnum.RUN_DOING) {
            log.error("workflow by id:{} is not running can not terminate", workflowRunStatus.getWorkflowId());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NOT_RUNNING.getMsg());
        }
        // 校验取消状态
        if (workflowRunStatus.getCancelStatus() == WorkflowTaskRunStatusEnum.RUN_NEED) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_CANCELING.getMsg());
        }
        if (workflowRunStatus.getCancelStatus() == WorkflowTaskRunStatusEnum.RUN_SUCCESS) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_CANCELLED_SUCCESS.getMsg());
        }
        if (workflowRunStatus.getCancelStatus() == WorkflowTaskRunStatusEnum.RUN_FAIL) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_CANCELLED_FAIL.getMsg());
        }

        workflowRunStatus.setCancelStatus(WorkflowTaskRunStatusEnum.RUN_NEED);
        // 更新工作流运行状态
        return workflowRunStatusManager.updateById(workflowRunStatus);
    }

    @Override
    public List<WorkflowRunTaskDto> getWorkflowRunTaskList(WorkflowRunKeyDto req) {
        List<WorkflowRunTaskStatus> workflowRunTaskStatusList = workflowRunTaskStatusManager.listByWorkflowRunId(req.getWorkflowRunId());
        AlgorithmClassify root = algService.getAlgTree(false, 1L);
        List<WorkflowRunTaskDto> result = workflowRunTaskStatusList.stream().map(item -> {
            WorkflowRunTaskDto workflowRunTaskDto = new WorkflowRunTaskDto();
            workflowRunTaskDto.setId(item.getId());
            workflowRunTaskDto.setTaskId(item.getTaskId());
            workflowRunTaskDto.setCreateTime(item.getCreateTime());
            WorkflowTask workflowTask = workflowTaskManager.getById(item.getId());
            AlgorithmClassify algorithmClassify = TreeUtils.findSubTree(root, workflowTask.getAlgorithmId());
            workflowRunTaskDto.setAlgorithmName(algorithmClassify.getName());
            workflowRunTaskDto.setAlgorithmNameEn(algorithmClassify.getNameEn());
            return workflowRunTaskDto;
        }).collect(Collectors.toList());
        return result;
    }
}
