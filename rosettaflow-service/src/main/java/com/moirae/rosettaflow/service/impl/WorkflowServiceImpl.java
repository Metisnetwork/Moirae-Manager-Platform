package com.moirae.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moirae.rosettaflow.common.enums.WorkflowCreateModeEnum;
import com.moirae.rosettaflow.grpc.task.req.dto.TaskEventDto;
import com.moirae.rosettaflow.manager.CalculationProcessManager;
import com.moirae.rosettaflow.manager.CalculationProcessStepManager;
import com.moirae.rosettaflow.manager.WorkflowManager;
import com.moirae.rosettaflow.manager.WorkflowVersionManager;
import com.moirae.rosettaflow.mapper.domain.CalculationProcess;
import com.moirae.rosettaflow.mapper.domain.Workflow;
import com.moirae.rosettaflow.mapper.domain.WorkflowVersion;
import com.moirae.rosettaflow.service.WorkflowService;
import com.moirae.rosettaflow.service.dto.task.TaskResultDto;
import com.moirae.rosettaflow.service.dto.workflow.*;
import com.moirae.rosettaflow.service.dto.workflow.expert.WorkflowNodeKeyDto;
import com.moirae.rosettaflow.service.dto.workflow.expert.WorkflowDetailsOfExpertModeDto;
import com.moirae.rosettaflow.service.dto.workflow.expert.WorkflowStatusOfExpertModeDto;
import com.moirae.rosettaflow.service.dto.workflow.wizard.WorkflowDetailsOfWizardModeDto;
import com.moirae.rosettaflow.service.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class WorkflowServiceImpl implements WorkflowService {

    @Resource
    private WorkflowManager workflowManager;
    @Resource
    private WorkflowVersionManager workflowVersionManager;
    @Resource
    private CalculationProcessManager calculationProcessManager;
    @Resource
    private CalculationProcessStepManager calculationProcessStepManager;

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
    public WorkflowVersionKeyDto createWorkflowOfWizardMode(Workflow workflow) {
        return create(workflow, WorkflowCreateModeEnum.WIZARD_MODE);
    }

    @Override
    public WorkflowVersionKeyDto settingWorkflowOfWizardMode(WorkflowDetailsOfWizardModeDto req) {
        return null;
    }

    @Override
    public WorkflowDetailsOfWizardModeDto getWorkflowSettingOfWizardMode(WorkflowVersionKeyDto req) {
        return null;
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
        WorkflowVersionKeyDto result = new WorkflowVersionKeyDto();

        // 创建工作流记录
        workflow.setCreateMode(createMode.ordinal());
        workflowManager.save(workflow);

        // 创建工作流版本
        WorkflowVersion workflowVersion = new WorkflowVersion();
        workflowVersion.setWorkflowId(workflow.getWorkflowId());
        workflowVersion.setWorkflowVersion(1L);
        workflowVersionManager.save(workflowVersion);

        // 向导模式创建任务
        if(createMode == WorkflowCreateModeEnum.WIZARD_MODE){
            createTaskOfWizard(workflow, workflowVersion);
        }
        return result;
    }

    private void createTaskOfWizard(Workflow workflow, WorkflowVersion workflowVersion) {
        // 查询流程定义


        // 生成向导配置


        // 生成任务


    }
}
