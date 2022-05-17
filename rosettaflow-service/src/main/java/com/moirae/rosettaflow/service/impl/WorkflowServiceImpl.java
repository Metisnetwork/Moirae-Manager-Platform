package com.moirae.rosettaflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.protobuf.ByteString;
import com.moirae.rosettaflow.common.constants.SysConfig;
import com.moirae.rosettaflow.common.enums.*;
import com.moirae.rosettaflow.common.exception.BusinessException;
import com.moirae.rosettaflow.common.utils.LanguageContext;
import com.moirae.rosettaflow.grpc.client.GrpcSysServiceClient;
import com.moirae.rosettaflow.grpc.client.GrpcTaskServiceClient;
import com.moirae.rosettaflow.grpc.constant.GrpcConstant;
import com.moirae.rosettaflow.grpc.dynamic.*;
import com.moirae.rosettaflow.grpc.service.*;
import com.moirae.rosettaflow.grpc.service.types.SimpleResponse;
import com.moirae.rosettaflow.grpc.service.types.TaskOrganization;
import com.moirae.rosettaflow.grpc.service.types.TaskResourceCostDeclare;
import com.moirae.rosettaflow.grpc.service.types.UserType;
import com.moirae.rosettaflow.manager.*;
import com.moirae.rosettaflow.mapper.domain.*;
import com.moirae.rosettaflow.mapper.enums.*;
import com.moirae.rosettaflow.mapper.enums.TaskStatusEnum;
import com.moirae.rosettaflow.service.*;
import com.moirae.rosettaflow.service.dto.alg.AlgDto;
import com.moirae.rosettaflow.service.dto.alg.AlgVariableV2Dto;
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
    private WorkflowRunTaskStatusManager workflowRunTaskStatusManager;
    @Resource
    private WorkflowRunTaskResultManager workflowRunTaskResultManager;

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
                    algorithm.getSupportDefaultPsi(), calculationProcessTask.getInputPsiStep());
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
        Algorithm algorithm = algService.getAlgorithm(workflowTask.getAlgorithmId(), false);

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
                        wizard.getTask1Step(), wizard.getTask2Step(),
                        root,
                        req.getTrainingInput().getIdentityId(), req.getTrainingInput().getIsPsi(), req.getTrainingInput().getItem(),
                        Optional.empty());
                break;
            case INPUT_PREDICTION:
                Optional<String> modelId = Optional.empty();
                if(req.getPredictionInput().getModel() != null){
                    modelId = Optional.of(req.getPredictionInput().getModel().getMetaDataId());
                }
                setInputOfWizardMode(req.getWorkflowId(), req.getWorkflowVersion(),
                        wizard.getTask1Step(), wizard.getTask2Step(),
                        root,
                        req.getPredictionInput().getIdentityId(), req.getPredictionInput().getIsPsi(), req.getPredictionInput().getItem(), modelId);
                break;
            case INPUT_PSI:
                setPsiInputOfWizardMode(req.getWorkflowId(), req.getWorkflowVersion(), wizard.getTask2Step(), req.getPsiInput().getIdentityId(), req.getPsiInput().getItem());
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

    private void setCommonOutputOfWizardMode(Long workflowId, Long workflowVersion, Integer taskStep, OutputDto commonOutput) {
        WorkflowTask workflowTask = workflowTaskManager.getByStep(workflowId, workflowVersion, taskStep);
        List<WorkflowTaskOutput> workflowTaskOutputList = convert2WorkflowTaskOutput(workflowTask.getWorkflowTaskId(), commonOutput);
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

    private TrainingInputDto getTrainingInputOfWizardMode(Long workflowId, Long workflowVersion, Integer task2Step) {
        TrainingInputDto trainingInputDto = new TrainingInputDto();
        WorkflowTask training = workflowTaskManager.getByStep(workflowId, workflowVersion, task2Step);
        trainingInputDto.setIdentityId(training.getIdentityId());
        trainingInputDto.setIsPsi(training.getInputPsi());
        trainingInputDto.setItem(BeanUtil.copyToList(workflowTaskInputManager.listByWorkflowTaskId(training.getWorkflowTaskId()), DataInputDto.class));
        return trainingInputDto;
    }

    private void setPsiInputOfWizardMode(Long workflowId, Long workflowVersion, Integer psiTaskStep, String senderIdentityId, List<DataInputDto> dataInputDtoList){
        WorkflowTask psi = workflowTaskManager.getByStep(workflowId, workflowVersion, psiTaskStep);
        psi.setIdentityId(senderIdentityId);
        workflowTaskManager.updateById(psi);

        List<WorkflowTaskInput> psiWorkflowTaskInputList =  convert2WorkflowTaskInput(psi.getWorkflowTaskId(), dataInputDtoList);
        workflowTaskInputManager.clearAndSave(psi.getWorkflowTaskId(), psiWorkflowTaskInputList);
    }

    private void setInputOfWizardMode(Long workflowId, Long workflowVersion, Integer psiTaskStep, Integer taskStep, AlgorithmClassify root, String senderIdentityId, Boolean isPsi,  List<DataInputDto> dataInputDtoList, Optional<String> modelId) {
        // 设置发起方和是否psi
        Map<WorkflowTaskInputTypeEnum, WorkflowTask> workflowTaskMap = workflowTaskManager.setWorkflowTask(workflowId, workflowVersion, psiTaskStep, taskStep, senderIdentityId, isPsi, modelId);
        WorkflowTask workflowTask = workflowTaskMap.get(WorkflowTaskInputTypeEnum.NORMAL);
        WorkflowTask psiWorkflowTask = workflowTaskMap.get(WorkflowTaskInputTypeEnum.PSI);

        // 设置训练及PSI的输入
        List<WorkflowTaskInput> workflowTaskInputList = convert2WorkflowTaskInput(workflowTask.getWorkflowTaskId(), dataInputDtoList);
        workflowTaskInputManager.setWorkflowTaskInput(psiWorkflowTask.getWorkflowTaskId(), workflowTask.getWorkflowTaskId(), workflowTaskInputList);

        // 设置PSI输出为数据的输入
        List<WorkflowTaskOutput> workflowTaskOutputList = convert2WorkflowTaskOutput(psiWorkflowTask.getWorkflowTaskId(), workflowTaskInputList, TreeUtils.findSubTree(root, psiWorkflowTask.getAlgorithmId()).getAlg());
        workflowTaskOutputManager.setWorkflowTaskOutput(psiWorkflowTask.getWorkflowTaskId(), workflowTaskOutputList);
    }


    private void setNodeInput(AlgorithmClassify root, AlgorithmClassify algorithmClassify, WorkflowTask psiWorkflowTask, WorkflowTask workflowTask, NodeInputDto nodeInput) {
        if(algorithmClassify.getAlg().getSupportDefaultPsi()){
            setInputOfWizardMode(workflowTask.getWorkflowId(), workflowTask.getWorkflowVersion(),
                    psiWorkflowTask.getStep(), workflowTask.getStep(),
                    root, nodeInput.getIdentityId(),
                    nodeInput.getIsPsi(), nodeInput.getDataInputList(),
                    algorithmClassify.getAlg().getInputModel()
                            && nodeInput.getModel() !=null
                            && !"frontNodeOutput".equals(nodeInput.getModel().getMetaDataId()) ? Optional.of(nodeInput.getModel().getMetaDataId()) : Optional.empty()
            );
        }else{
            setPsiInputOfWizardMode(workflowTask.getWorkflowId(), workflowTask.getWorkflowVersion(),
                    workflowTask.getStep(), nodeInput.getIdentityId(), nodeInput.getDataInputList());
        }

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
            AlgorithmClassify algorithmClassify = TreeUtils.findSubTree(root, nodeDto.getAlgorithmId());
            // 创建psi任务
            WorkflowTask psiWorkflowTask = null;
            if(algorithmClassify.getAlg().getSupportDefaultPsi()){
                // 创建psi设置
                psiWorkflowTask = workflowTaskManager.createOfWizardMode(
                        req.getWorkflowId(), req.getWorkflowVersion(), taskStep++,
                        psiAlgorithmClassify.getId(), false, null,
                        false, null);

                if(psiAlgorithmClassify.getAlg().getAlgorithmVariableList().size() > 0){
                    workflowTaskVariableManager.create(psiWorkflowTask.getWorkflowTaskId(), psiAlgorithmClassify.getAlg().getAlgorithmVariableList());
                }
            }

            // 创建任务
            WorkflowTask workflowTask = workflowTaskManager.createOfWizardMode(
                    req.getWorkflowId(), req.getWorkflowVersion(), taskStep++,
                    algorithmClassify.getId(), algorithmClassify.getAlg().getInputModel(),
                    algorithmClassify.getAlg().getInputModel() && ( nodeDto.getNodeInput().getModel() == null || "frontNodeOutput".equals(nodeDto.getNodeInput().getModel().getMetaDataId())) ? preStep : null,
                    nodeDto.getNodeInput().getIsPsi(), algorithmClassify.getAlg().getSupportDefaultPsi() ? psiWorkflowTask.getStep() : null);

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
                            && !"fromPreNodeOutput".equals(workflowTask.getInputModelId())){
                        nodeInputDto.setModel(BeanUtil.copyProperties(dataService.getModelById(workflowTask.getInputModelId()), ModelDto.class));
                    }
                    if(workflowTask.getInputModel()
                            && StringUtils.isBlank(workflowTask.getInputModelId())){
                        ModelDto modelDto = new ModelDto();
                        modelDto.setMetaDataId("fromPreNodeOutput");
                        nodeInputDto.setModel(modelDto);
                    }

                    nodeInputDto.setIsPsi(workflowTask.getInputPsi());
                    nodeInputDto.setDataInputList(BeanUtil.copyToList(workflowTaskInputManager.listByWorkflowTaskId(workflowTask.getWorkflowTaskId()), DataInputDto.class));
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
    public WorkflowFeeDto preparationStart(WorkflowVersionKeyDto req) {
        List<WorkflowTask> workflowTaskList = listExecutableDetailsByWorkflowVersion(req.getWorkflowId(), req.getWorkflowVersion());
        WorkflowFeeDto workflowFeeDto = new WorkflowFeeDto();
        workflowFeeDto.setWorkflowId(req.getWorkflowId());
        workflowFeeDto.setWorkflowVersion(req.getWorkflowVersion());
        workflowFeeDto.setItemList(convert2WorkflowFee(workflowTaskList));
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
        List<WorkflowFeeItemDto> workflowFeeList = convert2WorkflowFee(workflowTaskList);
        checkFee(workflowFeeList);
        // 保存运行时信息
        WorkflowRunStatus workflowRunStatus = createAndSaveWorkflowRunStatus(UserContext.getCurrentUser().getAddress(), req.getSign(), req.getWorkflowId(), req.getWorkflowVersion(), workflowTaskList);
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
                .setSender(publishTaskOfGetTaskOrganization(curWorkflowRunTaskStatus.getWorkflowTask().getOrg(), "sender1"))
                .setAlgoSupplier(publishTaskOfGetTaskOrganization(curWorkflowRunTaskStatus.getWorkflowTask().getOrg(), "algo1"));

        // 设置数据输入组织
        for (int i = 0; i < curWorkflowRunTaskStatus.getWorkflowTask().getInputList().size(); i++) {
            WorkflowTaskInput workflowTaskInput = curWorkflowRunTaskStatus.getWorkflowTask().getInputList().get(i);
            requestBuild.addDataSuppliers(publishTaskOfGetTaskOrganization(workflowTaskInput.getOrg(), workflowTaskInput.getPartyId()));
            requestBuild.addDataPolicyTypes(MetaDataFileTypeEnum.CSV.getValue());
            if(curWorkflowRunTaskStatus.getWorkflowTask().getInputPsi()){
                Psi psi = curWorkflowRunTaskStatus.getPsiList().stream()
                        .filter(item -> workflowTaskInput.getIdentityId().equals(item.getIdentityId()))
                        .findFirst().get();
                requestBuild.addDataPolicyOptions(createDataPolicyItem(workflowTaskInput, psi));
            }else{
                requestBuild.addDataPolicyOptions(createDataPolicyItem(workflowTaskInput));

            }
        }
        // 设置模型输入组织
        String modelPartyId = null;
        if(curWorkflowRunTaskStatus.getWorkflowTask().getInputModel()){
            modelPartyId = "data" + (requestBuild.getDataSuppliersBuilderList().size() + 1);
            requestBuild.addDataSuppliers(publishTaskOfGetTaskOrganization(curWorkflowRunTaskStatus.getModel().getOrg(), modelPartyId));
            requestBuild.addDataPolicyTypes(2);
            requestBuild.addDataPolicyOptions(createDataPolicyItem(curWorkflowRunTaskStatus.getModel(), modelPartyId));
        }

        // 接收方策略
        for (int i = 0; i < curWorkflowRunTaskStatus.getWorkflowTask().getOutputList().size(); i++) {
            WorkflowTaskOutput workflowTaskOutput = curWorkflowRunTaskStatus.getWorkflowTask().getOutputList().get(i);
            requestBuild.addReceivers(publishTaskOfGetTaskOrganization(workflowTaskOutput.getOrg(), workflowTaskOutput.getPartyId()));
            if(curWorkflowRunTaskStatus.getWorkflowTask().getAlgorithm().getAlgorithmId() == sysConfig.getDefaultPsi()){
                requestBuild.addReceiverPolicyTypes(2);
                requestBuild.addReceiverPolicyOptions(createPowerPolicy2Item(workflowTaskOutput, curWorkflowRunTaskStatus.getWorkflowTask().getInputList()));
            } else {
                requestBuild.addReceiverPolicyTypes(1);
                requestBuild.addReceiverPolicyOptions(workflowTaskOutput.getPartyId());
            }
        }

        // 算力策略
        if(curWorkflowRunTaskStatus.getWorkflowTask().getAlgorithm().getAlgorithmId() == sysConfig.getDefaultPsi()){
            // 如果是psi算法，指定数据节点提供算力策略
            for (int i = 0; i < curWorkflowRunTaskStatus.getWorkflowTask().getInputList().size(); i++) {
                WorkflowTaskInput workflowTaskInput = curWorkflowRunTaskStatus.getWorkflowTask().getInputList().get(i);
                requestBuild.addPowerPolicyTypes(2);
                requestBuild.addPowerPolicyOptions(createPowerPolicy2Item(workflowTaskInput));
            }
        }else{
            // 随机算力
            requestBuild.addPowerPolicyTypes(1);
            requestBuild.addPowerPolicyOptions("compute1");
            requestBuild.addPowerPolicyTypes(1);
            requestBuild.addPowerPolicyOptions("compute2");
            requestBuild.addPowerPolicyTypes(1);
            requestBuild.addPowerPolicyOptions("compute3");
        }

        // data_flow_policy_type & data_flow_policy_option
        if(curWorkflowRunTaskStatus.getWorkflowTask().getAlgorithm().getAlgorithmId() == sysConfig.getDefaultPsi()){
            // psi 需要指定
            requestBuild.addDataFlowPolicyTypes(2);
            requestBuild.addDataFlowPolicyOptions(createDataFlowPolicy2(curWorkflowRunTaskStatus.getWorkflowTask().getInputList(), curWorkflowRunTaskStatus.getWorkflowTask().getOutputList()));
        }else{
            // 其他全连接
            requestBuild.addDataFlowPolicyTypes(1);
            requestBuild.addDataFlowPolicyOptions(createDataFlowPolicy1());
        }


        WorkflowTaskResource resource = curWorkflowRunTaskStatus.getWorkflowTask().getResource();
        TaskResourceCostDeclare taskResourceCostDeclare = TaskResourceCostDeclare.newBuilder()
                .setMemory(resource.getCostMem())
                .setProcessor(resource.getCostCpu())
                .setBandwidth(resource.getCostBandwidth())
                .setDuration(resource.getRunTime())
                .build();
        requestBuild.setOperationCost(taskResourceCostDeclare);

        requestBuild.setAlgorithmCode(curWorkflowRunTaskStatus.getWorkflowTask().getAlgorithm().getAlgorithmCode().getCalculateContractCode());
        requestBuild.setMetaAlgorithmId("");

        // 如果是psi算法，指定数据节点提供算力策略
        if(curWorkflowRunTaskStatus.getWorkflowTask().getAlgorithm().getAlgorithmId() == sysConfig.getDefaultPsi()){
            requestBuild.setAlgorithmCodeExtraParams(
                    createAlgorithmCodeExtraParamsForPsi(
                            curWorkflowRunTaskStatus.getWorkflowTask().getAlgorithm().getAlgorithmCode().getCalculateContractStruct(),
                            curWorkflowRunTaskStatus.getWorkflowTask().getInputList(),
                            curWorkflowRunTaskStatus.getWorkflowTask().getOutputList()));
        }else{
            requestBuild.setAlgorithmCodeExtraParams(createAlgorithmCodeExtraParams(curWorkflowRunTaskStatus.getWorkflowTask().getAlgorithm().getAlgorithmCode().getCalculateContractStruct(),
                    curWorkflowRunTaskStatus.getWorkflowTask().getVariableList(), curWorkflowRunTaskStatus.getWorkflowTask().getInputPsi(),
                    curWorkflowRunTaskStatus.getWorkflowTask().getInputList().stream().filter(item -> item.getDependentVariable() != null && item.getDependentVariable() > 0
                    ).findFirst(), modelPartyId, curWorkflowRunTaskStatus.getWorkflowTask().getAlgorithm()));
        }

        requestBuild.setSign(ByteString.copyFromUtf8(workflowRunStatus.getSign()));
        requestBuild.setDesc("");
        return requestBuild.build();
    }

    private String createDataFlowPolicy1() {
        return "{}";
    }

    private String createDataFlowPolicy2(List<WorkflowTaskInput> inputList, List<WorkflowTaskOutput> outputList) {
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
        return connectPolicy.toJSONString();
    }


    private String createAlgorithmCodeExtraParamsForPsi(String calculateContractStruct, List<WorkflowTaskInput> workflowTaskInputList,  List<WorkflowTaskOutput> workflowTaskOutputList) {
        JSONObject algorithmDynamicParams = JSONObject.parseObject(calculateContractStruct);

        if(algorithmDynamicParams.containsKey("use_alignment")){
            boolean useAlignment =  workflowTaskInputList.stream()
                    .filter(item-> StringUtils.isNotBlank(item.getDataColumnIds()))
                    .count() > 0 ? true : false;
            algorithmDynamicParams.put("use_alignment", useAlignment);
        }

        workflowTaskInputList.stream()
            .filter(item -> item.getDependentVariable() != null && item.getDependentVariable() > 0)
            .findFirst().ifPresent(item -> {
                algorithmDynamicParams.put("label_owner", item.getPartyId());
                algorithmDynamicParams.put("label_column", dataService.getDataColumnByIds(item.getMetaDataId(), item.getDependentVariable().intValue()).getColumnName());
        });

        if(algorithmDynamicParams.containsKey("data_flow_restrict")){
            JSONObject dataFlowRestrict = new JSONObject();
            // 输入
            Map<String, WorkflowTaskInput> workflowTaskInputMap = new HashMap<>();
            for (WorkflowTaskInput workflowTaskInput : workflowTaskInputList) {
                JSONArray value1 = new JSONArray();
                value1.add(StringUtils.replace(workflowTaskInput.getPartyId(), "data", "compute"));
                dataFlowRestrict.put(workflowTaskInput.getPartyId(), value1 );
                workflowTaskInputMap.put(workflowTaskInput.getIdentityId(), workflowTaskInput);
            }
            // 计算
            for (WorkflowTaskOutput workflowTaskOutput: workflowTaskOutputList) {
                WorkflowTaskInput workflowTaskInput = workflowTaskInputMap.get(workflowTaskOutput.getIdentityId());

                JSONArray value1 = new JSONArray();
                value1.add(workflowTaskOutput.getPartyId());
                dataFlowRestrict.put(StringUtils.replace(workflowTaskInput.getPartyId(), "data", "compute"), value1);
            }
            algorithmDynamicParams.put("data_flow_restrict", dataFlowRestrict);
        }
        return algorithmDynamicParams.toJSONString();
    }

    private String createAlgorithmCodeExtraParams(String calculateContractStruct, List<WorkflowTaskVariable> variableList, Boolean usePsi, Optional<WorkflowTaskInput> workflowTaskInput, String modelRestoreParty, Algorithm algorithm) {
        JSONObject algorithmDynamicParams = JSONObject.parseObject(calculateContractStruct);
        //是否使用psi
        if(algorithmDynamicParams.containsKey("use_psi")){
            algorithmDynamicParams.put("use_psi", usePsi);
        }
        //标签所在方的party_id
        if(algorithmDynamicParams.containsKey("label_owner")){
            workflowTaskInput.ifPresent(item -> {
                algorithmDynamicParams.put("label_owner", item.getPartyId());
            });

        }
        // 因变量(标签)
        if(algorithmDynamicParams.containsKey("label_column")){
            workflowTaskInput.ifPresent(item -> {
                algorithmDynamicParams.put("label_column", dataService.getDataColumnByIds(item.getMetaDataId(), item.getDependentVariable().intValue()).getColumnName());
            });
        }
        // 模型所在方
        if(algorithmDynamicParams.containsKey("model_restore_party")){
            algorithmDynamicParams.put("model_restore_party", modelRestoreParty);
        }
        if(algorithmDynamicParams.containsKey("hyperparams")){
            JSONObject hyperParams = algorithmDynamicParams.getJSONObject("hyperparams");
            Map<String, AlgorithmVariableTypeEnum> variableTypeEnumMap = algorithm.getAlgorithmVariableList().stream().collect(Collectors.toMap(AlgorithmVariable::getVarKey, AlgorithmVariable::getVarType));
            variableList.forEach(workflowTaskVariable -> hyperParams.put(workflowTaskVariable.getVarKey(), convert2HyperParams(workflowTaskVariable, variableTypeEnumMap)));
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

    private String createPowerPolicy2Item(WorkflowTaskOutput workflowTaskOutput, List<WorkflowTaskInput> inputList) {
        TaskReceiverPolicy2 taskReceiverPolicy2 = new TaskReceiverPolicy2();
        taskReceiverPolicy2.setProviderPartyId(inputList.stream().filter(item -> item.getIdentityId().equals(workflowTaskOutput.getIdentityId())).findFirst().get().getPartyId());
        taskReceiverPolicy2.setReceiverPartyId(workflowTaskOutput.getPartyId());
        return JSONObject.toJSONString(taskReceiverPolicy2);

    }

    private String createPowerPolicy2Item(WorkflowTaskInput workflowTaskInput) {
        TaskPowerPolicy2 taskPowerPolicy2 = new TaskPowerPolicy2();
        taskPowerPolicy2.setProviderPartyId(workflowTaskInput.getPartyId());
        taskPowerPolicy2.setPowerPartyId(StringUtils.replace(workflowTaskInput.getPartyId(), "data", "compute"));
        return JSONObject.toJSONString(taskPowerPolicy2);
    }

    private String createDataPolicyItem(Model model, String partyId) {
        TaskDataPolicyUnknown dataPolicy = new TaskDataPolicyUnknown();
        dataPolicy.setInputType(2);
        dataPolicy.setPartyId(partyId);
        dataPolicy.setMetadataId(model.getMetaDataId());
        dataPolicy.setMetadataName(model.getName());
        return JSONObject.toJSONString(dataPolicy);
    }

    private String createDataPolicyItem(WorkflowTaskInput workflowTaskInput, Psi psi) {
        TaskDataPolicyCsv dataPolicy = new TaskDataPolicyCsv();
        dataPolicy.setInputType(1);
        dataPolicy.setPartyId(workflowTaskInput.getPartyId());
        dataPolicy.setMetadataId(psi.getMetaDataId());
        dataPolicy.setMetadataName(psi.getName());
        dataPolicy.setKeyColumn(workflowTaskInput.getKeyColumn());
        List<Integer> selectedColumns = new ArrayList<>();
        if(StringUtils.isNotBlank(workflowTaskInput.getDataColumnIds())){
            MetadataOptionCsv metadataOptionCsv = JSONObject.parseObject(psi.getMetadataOption(), MetadataOptionCsv.class);
            Map<String, MetadataOptionCsv.CsvColumns> stringCsvColumnsMap = metadataOptionCsv
                    .getMetadataColumns()
                    .stream()
                    .collect(Collectors.toMap(MetadataOptionCsv.CsvColumns::getName, me -> me));

            List<Integer> selectedColumnsV2 = new ArrayList<>();
            Arrays.stream(workflowTaskInput.getDataColumnIds().split(",")).forEach(subItem -> {
                selectedColumnsV2.add(Integer.valueOf(subItem));
            });
            List<MetaDataColumn> metaDataColumnList = dataService.listMetaDataColumnByIdAndIndex(workflowTaskInput.getMetaDataId(), selectedColumnsV2);
            Map<Integer, MetaDataColumn> metaDataColumnMap = metaDataColumnList.stream().collect(Collectors.toMap(MetaDataColumn::getColumnIdx, me -> me));

            selectedColumnsV2.forEach(item -> {
                selectedColumns.add(stringCsvColumnsMap.get(metaDataColumnMap.get(item).getColumnName()).getIndex());
            });
        }
        dataPolicy.setSelectedColumns(selectedColumns);
        return JSONObject.toJSONString(dataPolicy);
    }

    private String createDataPolicyItem(WorkflowTaskInput workflowTaskInput) {
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
        return JSONObject.toJSONString(dataPolicy);
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
        WorkflowCreateModeEnum createModeEnum = workflowRunStatus.getWorkflow().getCreateMode();
        return StringUtils.abbreviate(id+ "_" + address + "_" + algorithmName + "_" + createModeEnum.getValue() + "_" + workflowName, 100);
    }

    private WorkflowRunStatus loadWorkflowRunStatus(WorkflowRunStatus workflowRunStatus, List<WorkflowTask> workflowTaskList) {
        Map<Long, WorkflowTask> workflowTaskMap = workflowTaskList.stream().collect(Collectors.toMap(WorkflowTask::getWorkflowTaskId, me -> me));
        workflowRunStatus.setWorkflow(workflowManager.getById(workflowRunStatus.getWorkflowId()));
        List<WorkflowRunTaskStatus> workflowRunTaskStatusList = workflowRunTaskStatusManager.listByWorkflowRunId(workflowRunStatus.getId());
        workflowRunTaskStatusList
                .forEach(item -> {
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
        workflowRunStatus.setWorkflow(workflowManager.getById(workflowId));
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
    private void checkFee(List<WorkflowFeeItemDto> workflowFeeList) {
        long count = workflowFeeList.stream()
                .filter(item -> !item.getIsEnough()).count();
        if( count > 0) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_EXECUTE_VALUE_INSUFFICIENT.getMsg());
        }
    }

    private List<WorkflowFeeItemDto> convert2WorkflowFee(List<WorkflowTask> workflowTaskList) {
        // 白名单校验
        Set<String> senderSet = workflowTaskList.stream().map(WorkflowTask::getIdentityId).collect(Collectors.toSet());
        Map<String, Org> userOrgMap = orgService.getUserOrgList().stream()
                .filter(item -> senderSet.contains(item.getIdentityId()))
                .collect(Collectors.toMap(Org::getIdentityId, me -> me));
        List<String> errorList = new ArrayList<>();
        for (String sender : senderSet){
            if(!userOrgMap.containsKey(sender) || ! userOrgMap.get(sender).getIsInWhitelist()){
                errorList.add(sender);
            }
        }
        if(errorList.size() > 0){
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, StringUtils.replace(ErrorMsg.ORGANIZATION_NOT_IN_WHITE_LIST.getMsg(), "{}", errorList.toString()));
        }

        // token费用
        Map<String, Long> metaDataId2CountMap = workflowTaskList.stream()
                .flatMap(item -> item.getInputList().stream())
                .collect(Collectors.groupingBy(WorkflowTaskInput::getMetaDataId, Collectors.counting()));
        List<MetaData> metaDataList = dataService.listMetaDataByIds(metaDataId2CountMap.keySet());
        Map<String, String> metaDataId2TokenAddressMap = metaDataList.stream()
                .collect(Collectors.toMap(MetaData::getMetaDataId, MetaData::getTokenAddress));
        List<Token> tokenList = dataService.listTokenByIds(metaDataId2TokenAddressMap.values());
        tokenList.add(dataService.getMetisToken());
        Map<String, Token> tokenId2TokenMap = tokenList.stream().collect(Collectors.toMap(Token::getAddress, item -> item));
        List<WorkflowFeeItemDto> tokenFeeList =  metaDataId2CountMap.entrySet().stream()
                .map(item -> createWorkflowTaskFeeItemDto(WorkflowPayTypeEnum.TOKEN, new BigInteger(sysConfig.getDefaultTokenValue()).multiply(BigInteger.valueOf(item.getValue())).toString(), tokenId2TokenMap.get(metaDataId2TokenAddressMap.get(item.getKey()))))
                .collect(Collectors.toList());
        // 手续费
        List<WorkflowFeeItemDto> feeList = workflowTaskList.stream()
                .map(item -> {
                    List<String> tokenIdList = item.getInputList().stream().map(subItem-> metaDataId2TokenAddressMap.get(subItem.getMetaDataId())).collect(Collectors.toList());
                    EstimateTaskGasRequest.Builder requestBuilder = EstimateTaskGasRequest.newBuilder();
                    requestBuilder.setTaskSponsorAddress(UserContext.getCurrentUser().getAddress());
                    tokenIdList.forEach(subItem -> requestBuilder.addDataTokenAddresses(subItem));
                    EstimateTaskGasResponse response = grpcTaskServiceClient.estimateTaskGas(orgService.getChannel(item.getIdentityId()), requestBuilder.build());
                    WorkflowFeeItemDto workflowTaskFeeItemDto = createWorkflowTaskFeeItemDto(WorkflowPayTypeEnum.FEE, BigInteger.valueOf(response.getGasLimit()).multiply(BigInteger.valueOf(response.getGasPrice())).toString(), dataService.getMetisToken());
                    return workflowTaskFeeItemDto;
                })
                .collect(Collectors.toList());

        tokenFeeList.addAll(feeList);
        return tokenFeeList;
    }

    private WorkflowFeeItemDto createWorkflowTaskFeeItemDto(WorkflowPayTypeEnum typeEnum, String value, Token token){
        WorkflowFeeItemDto workflowFeeItemDto = new WorkflowFeeItemDto();
        workflowFeeItemDto.setType(typeEnum);
        workflowFeeItemDto.setNeedValue(value);
        workflowFeeItemDto.setToken(BeanUtil.copyProperties(token, TokenDto.class));
        workflowFeeItemDto.setTokenHolder(BeanUtil.copyProperties(dataService.getTokenHolderById(token.getAddress(), UserContext.getCurrentUser().getAddress()), TokenHolderDto.class));
        workflowFeeItemDto.setIsEnough(
                new BigDecimal(workflowFeeItemDto.getTokenHolder().getBalance()).compareTo(new BigDecimal(workflowFeeItemDto.getNeedValue()))>0
                        && new BigDecimal(workflowFeeItemDto.getTokenHolder().getAuthorizeBalance()).compareTo(new BigDecimal(workflowFeeItemDto.getNeedValue())) >= 0);
        return workflowFeeItemDto;
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
            String taskId = workflowRunTaskStatusManager.getByWorkflowRunIdAndStep(curWorkflowRunTaskStatus.getWorkflowRunId(), workflowTask.getInputPsiStep()).getTaskId();
            List<Psi> psiList = dataService.listPsiByTrainTaskId(taskId);
            curWorkflowRunTaskStatus.setPsiList(psiList);
            // psi的组织
            curWorkflowRunTaskStatus.getPsiList().forEach(item -> {
                item.setOrg(orgService.getOrgById(item.getIdentityId()));
            });
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
        List<WorkflowRunTaskStatus> workflowRunTaskStatusList = workflowRunTaskStatusManager.listByWorkflowRunId(req.getWorkflowRunId());
        WorkflowVersion workflowVersion = workflowVersionManager.getById(workflowRunStatus.getWorkflowId(), workflowRunStatus.getWorkflowVersion());
        AlgorithmClassify root = algService.getAlgorithmClassifyTree(false);
        List<WorkflowRunTaskDto> result = workflowRunTaskStatusList.stream().map(item -> {
            WorkflowRunTaskDto workflowRunTaskDto = new WorkflowRunTaskDto();
            workflowRunTaskDto.setId(item.getId());
            workflowRunTaskDto.setTaskId(item.getTaskId());
            workflowRunTaskDto.setCreateTime(item.getCreateTime());
            WorkflowTask workflowTask = workflowTaskManager.getById(item.getId());
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

    private void checkWorkFlowOnlyOwner(Workflow workflow){
        // 只有拥有者才可以终止
        if(!workflow.getAddress().equals(UserContext.getCurrentUser().getAddress())){
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_ONLY_OWNER_OPERATE.getMsg());
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


