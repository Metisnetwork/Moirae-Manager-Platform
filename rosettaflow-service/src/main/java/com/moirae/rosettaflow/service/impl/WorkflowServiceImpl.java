package com.moirae.rosettaflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moirae.rosettaflow.common.utils.LanguageContext;
import com.moirae.rosettaflow.grpc.task.req.dto.TaskEventDto;
import com.moirae.rosettaflow.manager.*;
import com.moirae.rosettaflow.mapper.domain.*;
import com.moirae.rosettaflow.mapper.enums.WorkflowCreateModeEnum;
import com.moirae.rosettaflow.service.AlgService;
import com.moirae.rosettaflow.service.DataService;
import com.moirae.rosettaflow.service.WorkflowService;
import com.moirae.rosettaflow.service.dto.model.ModelDto;
import com.moirae.rosettaflow.service.dto.task.TaskResultDto;
import com.moirae.rosettaflow.service.dto.workflow.*;
import com.moirae.rosettaflow.service.dto.workflow.common.DataInputDto;
import com.moirae.rosettaflow.service.dto.workflow.common.OutputDto;
import com.moirae.rosettaflow.service.dto.workflow.common.ResourceDto;
import com.moirae.rosettaflow.service.dto.workflow.expert.WorkflowDetailsOfExpertModeDto;
import com.moirae.rosettaflow.service.dto.workflow.expert.WorkflowNodeKeyDto;
import com.moirae.rosettaflow.service.dto.workflow.expert.WorkflowStatusOfExpertModeDto;
import com.moirae.rosettaflow.service.dto.workflow.wizard.*;
import com.moirae.rosettaflow.service.utils.CommonUtils;
import com.moirae.rosettaflow.service.utils.TreeUtils;
import com.moirae.rosettaflow.service.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WorkflowServiceImpl implements WorkflowService {

    @Resource
    private AlgService algService;
    @Resource
    private DataService dataService;
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

    @Override
    public int getWorkflowCount() {
        return workflowManager.getWorkflowCount(UserContext.getCurrentUser().getAddress());
    }

    @Override
    public IPage<Workflow> getWorkflowList(Long current, Long size, String keyword, Long algorithmId, Date begin, Date end) {
        Page<Workflow> page = new Page<>(current, size);
        return workflowManager.getWorkflowList(page, UserContext.getCurrentUser().getAddress(), keyword, algorithmId, begin, end);
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
        WorkflowVersion workflowVersion = new WorkflowVersion();
        workflowVersion.setWorkflowId(workflow.getWorkflowId());
        workflowVersion.setWorkflowVersion(1L);
        workflowVersion.setWorkflowVersionName(StringUtils.join(workflowName, "-v1"));
        workflowVersionManager.save(workflowVersion);

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
                    initWorkflowTaskCodeOfWizardMode(workflowTask.getWorkflowTaskId(), algorithm);
                    initWorkflowTaskVariableOfWizardMode(workflowTask.getWorkflowTaskId(), algorithm);
                    initWorkflowTaskResourceOfWizardMode(workflowTask.getWorkflowTaskId(), algorithm);
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

    private void initWorkflowTaskResourceOfWizardMode(Long workflowTaskId, Algorithm algorithm) {
        WorkflowTaskResource workflowTaskResource = new WorkflowTaskResource();
        workflowTaskResource.setWorkflowTaskId(workflowTaskId);
        workflowTaskResource.setCostCpu(algorithm.getCostCpu());
        workflowTaskResource.setCostGpu(algorithm.getCostGpu());
        workflowTaskResource.setCostMem(algorithm.getCostMem());
        workflowTaskResource.setCostBandwidth(algorithm.getCostBandwidth());
        workflowTaskResource.setRunTime(algorithm.getRunTime());
        workflowTaskResourceManager.save(workflowTaskResource);
    }

    private void initWorkflowTaskVariableOfWizardMode(Long workflowTaskId, Algorithm algorithm) {
        if(algorithm.getAlgorithmVariableList().size() == 0){
            return;
        }

        List<WorkflowTaskVariable> workflowTaskVariableList = algorithm.getAlgorithmVariableList().stream()
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

    private void initWorkflowTaskCodeOfWizardMode(Long workflowTaskId, Algorithm algorithm) {
        if(algorithm.getAlgorithmCode() == null){
            return;
        }
        WorkflowTaskCode workflowTaskCode = new WorkflowTaskCode();
        workflowTaskCode.setWorkflowTaskId(workflowTaskId);
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

    private WorkflowTask createTask(int step, AlgorithmClassify algorithmClassify, boolean enable) {

        return null;
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
            outputDto.setStorePattern(algService.getAlg(workflowTask.getAlgorithmId()).getStorePattern());
        }
        outputDto.setIdentityId(workflowTaskOutputList.stream().map(WorkflowTaskOutput::getIdentityId).collect(Collectors.toList()));
        return outputDto;
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
            workflowTaskOutput.setStorePattern(algService.getAlg(algorithmId).getStorePattern());
            workflowTaskOutput.setPartyId("q" + i);
            workflowTaskOutputList.add(workflowTaskOutput);
        }
        workflowTaskOutputManager.clearAndSave(workflowTaskId, workflowTaskOutputList);
    }


    @Override
    public WorkflowVersionKeyDto createWorkflowOfExpertMode(Workflow workflow) {
        return create(workflow, WorkflowCreateModeEnum.EXPERT_MODE);
    }

    @Override
    public WorkflowVersionKeyDto settingWorkflowOfExpertMode(WorkflowDetailsOfExpertModeDto req) {
        return null;
    }

    @Override
    public WorkflowDetailsOfExpertModeDto getWorkflowSettingOfExpertMode(WorkflowVersionKeyDto req) {
        return null;
    }

    @Override
    public WorkflowStatusOfExpertModeDto getWorkflowStatusOfExpertMode(WorkflowVersionKeyDto req) {
        return null;
    }

    @Override
    public List<TaskEventDto> getWorkflowLogOfExpertMode(WorkflowVersionKeyDto req) {
        return null;
    }

    @Override
    public List<TaskResultDto> getWorkflowNodeResult(WorkflowNodeKeyDto req) {
        return null;
    }

    @Override
    public WorkflowVersionKeyDto copyWorkflow(WorkflowVersionKeyDto req) {
        return null;
    }

    @Override
    public Boolean deleteWorkflow(WorkflowKeyDto req) {
        return null;
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
    public List<WorkflowFeeDto> estimateWorkflowFee(WorkflowVersionKeyDto req) {
        return null;
    }

    @Override
    public IPage<WorkflowRunTaskDto> getWorkflowRunTaskList(WorkflowRunKeyDto req) {
        return null;
    }

    private WorkflowVersionKeyDto create(Workflow workflow, WorkflowCreateModeEnum createMode){
        return null;
    }
}
