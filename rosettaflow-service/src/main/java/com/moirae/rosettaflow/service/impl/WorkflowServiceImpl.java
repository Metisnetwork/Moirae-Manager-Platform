package com.moirae.rosettaflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moirae.rosettaflow.common.enums.ErrorMsg;
import com.moirae.rosettaflow.common.enums.OldAndNewEnum;
import com.moirae.rosettaflow.common.enums.RespCodeEnum;
import com.moirae.rosettaflow.common.enums.WorkflowPayTypeEnum;
import com.moirae.rosettaflow.common.exception.BusinessException;
import com.moirae.rosettaflow.common.utils.LanguageContext;
import com.moirae.rosettaflow.grpc.client.GrpcTaskServiceClient;
import com.moirae.rosettaflow.grpc.service.DataTokenTransferItem;
import com.moirae.rosettaflow.grpc.service.EstimateTaskGasRequest;
import com.moirae.rosettaflow.grpc.service.EstimateTaskGasResponse;
import com.moirae.rosettaflow.manager.*;
import com.moirae.rosettaflow.mapper.domain.*;
import com.moirae.rosettaflow.mapper.enums.CalculationProcessTypeEnum;
import com.moirae.rosettaflow.mapper.enums.WorkflowCreateModeEnum;
import com.moirae.rosettaflow.mapper.enums.WorkflowTaskRunStatusEnum;
import com.moirae.rosettaflow.service.*;
import com.moirae.rosettaflow.service.dto.data.MetisLatInfoDto;
import com.moirae.rosettaflow.service.dto.model.ModelDto;
import com.moirae.rosettaflow.service.dto.org.OrgNameDto;
import com.moirae.rosettaflow.service.dto.task.TaskEventDto;
import com.moirae.rosettaflow.service.dto.task.TaskResultDto;
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
    public Boolean clearWorkflow(WorkflowVersionKeyDto req) {
        return null;
    }

    @Override
    public WorkflowRunKeyDto start(WorkflowStartSignatureDto req) {
        return null;
    }

    @Override
    public Boolean terminate(WorkflowRunKeyDto req) {
        return null;
    }

    @Override
    public List<WorkflowFeeDto> preparationStart(WorkflowVersionKeyDto req) {
        List<WorkflowTask> workflowTaskList = workflowTaskManager.listExecutableByWorkflowVersion(req.getWorkflowId(), req.getWorkflowVersion());
        return workflowTaskList.stream()
                .map(item -> {
                    WorkflowFeeDto workflowFeeDto = new WorkflowFeeDto();
                    workflowFeeDto.setWorkflowId(req.getWorkflowId());
                    workflowFeeDto.setWorkflowVersion(req.getWorkflowVersion());
                    workflowFeeDto.setWorkflowTaskId(item.getWorkflowTaskId());
                    List<WorkflowTaskFeeItemDto> workflowTaskFeeItemDtoList = new ArrayList<>();
                    List<WorkflowTaskInput> workflowTaskInputList = workflowTaskInputManager.listByWorkflowTaskId(item.getWorkflowTaskId());

                    List<DataTokenTransferItem>  dataTokenTransferItemList = new ArrayList<>();
                    for (WorkflowTaskInput workflowTaskInput : workflowTaskInputList) {
                        WorkflowTaskFeeItemDto workflowTaskFeeItemDto = new WorkflowTaskFeeItemDto();
                        workflowTaskFeeItemDto.setType(WorkflowPayTypeEnum.TOKEN);
                        workflowTaskFeeItemDto.setNeedValue("1000000000000000000");
                        workflowTaskFeeItemDto.setToken(dataService.getTokenByMetaDataId(workflowTaskInput.getMetaDataId()));
                        workflowTaskFeeItemDto.setTokenHolder(dataService.getTokenHolderById(workflowTaskFeeItemDto.getToken().getAddress(), UserContext.getCurrentUser().getAddress()));
                        workflowTaskFeeItemDto.setIsEnough(new BigDecimal(workflowTaskFeeItemDto.getTokenHolder().getBalance()).compareTo(new BigDecimal(workflowTaskFeeItemDto.getNeedValue()))>0
                                && new BigDecimal(workflowTaskFeeItemDto.getTokenHolder().getAuthorizeBalance()).compareTo(new BigDecimal(workflowTaskFeeItemDto.getNeedValue())) >= 0);
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
                    WorkflowTaskFeeItemDto workflowTaskFeeItemDto = new WorkflowTaskFeeItemDto();
                    workflowTaskFeeItemDto.setType(WorkflowPayTypeEnum.FEE);
                    workflowTaskFeeItemDto.setNeedValue(BigInteger.valueOf(response.getGasLimit()).multiply(BigInteger.valueOf(response.getGasPrice())).toString());
                    workflowTaskFeeItemDto.setToken(dataService.getMetisToken());
                    workflowTaskFeeItemDto.setTokenHolder(dataService.getTokenHolderById(workflowTaskFeeItemDto.getToken().getAddress(), UserContext.getCurrentUser().getAddress()));
                    workflowTaskFeeItemDto.setIsEnough(new BigDecimal(workflowTaskFeeItemDto.getTokenHolder().getBalance()).compareTo(new BigDecimal(workflowTaskFeeItemDto.getNeedValue()))>0
                            && new BigDecimal(workflowTaskFeeItemDto.getTokenHolder().getAuthorizeBalance()).compareTo(new BigDecimal(workflowTaskFeeItemDto.getNeedValue())) >= 0);
                    workflowTaskFeeItemDtoList.add(workflowTaskFeeItemDto);
                    workflowTaskFeeItemDtoList.add(workflowTaskFeeItemDto);
                    workflowFeeDto.setItemList(workflowTaskFeeItemDtoList);
                    return workflowFeeDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public IPage<WorkflowRunTaskDto> getWorkflowRunTaskList(WorkflowRunKeyDto req) {
        return null;
    }



}
