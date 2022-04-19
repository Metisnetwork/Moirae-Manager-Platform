package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moirae.rosettaflow.mapper.domain.CalculationProcess;
import com.moirae.rosettaflow.mapper.domain.Workflow;
import com.moirae.rosettaflow.mapper.domain.WorkflowVersion;
import com.moirae.rosettaflow.service.dto.task.TaskEventDto;
import com.moirae.rosettaflow.service.dto.task.TaskResultDto;
import com.moirae.rosettaflow.service.dto.workflow.*;
import com.moirae.rosettaflow.service.dto.workflow.expert.WorkflowNodeKeyDto;
import com.moirae.rosettaflow.service.dto.workflow.expert.WorkflowDetailsOfExpertModeDto;
import com.moirae.rosettaflow.service.dto.workflow.expert.WorkflowStatusOfExpertModeDto;
import com.moirae.rosettaflow.service.dto.workflow.wizard.WorkflowDetailsOfWizardModeDto;
import com.moirae.rosettaflow.service.dto.workflow.wizard.WorkflowWizardStepDto;

import java.util.Date;
import java.util.List;

public interface WorkflowService{

    int getWorkflowCount();

    IPage<Workflow> getWorkflowList(Long current, Long size, String keyword, Long algorithmId, Date begin, Date end);

    IPage<WorkflowVersion> getWorkflowVersionList(Long current, Long size, Long workflowId);

    List<CalculationProcess> getCalculationProcessList(Long algorithmId);

    WorkflowVersionKeyDto settingWorkflowOfWizardMode(WorkflowDetailsOfWizardModeDto req);

    WorkflowDetailsOfWizardModeDto getWorkflowSettingOfWizardMode(WorkflowWizardStepDto req);

    WorkflowVersionKeyDto settingWorkflowOfExpertMode(WorkflowDetailsOfExpertModeDto req);

    WorkflowDetailsOfExpertModeDto getWorkflowSettingOfExpertMode(WorkflowVersionKeyDto req);

    WorkflowStatusOfExpertModeDto getWorkflowStatusOfExpertMode(WorkflowVersionKeyDto req);

    List<TaskEventDto> getWorkflowLogOfExpertMode(WorkflowVersionKeyDto req);

    List<TaskResultDto> getWorkflowNodeResult(WorkflowNodeKeyDto req);

    WorkflowVersionKeyDto copyWorkflow(WorkflowVersionNameDto req);

    Boolean deleteWorkflow(WorkflowKeyDto req);

    WorkflowRunKeyDto start(WorkflowStartSignatureDto req);

    Boolean terminate(WorkflowRunKeyDto req);

    List<WorkflowFeeDto> preparationStart(WorkflowVersionKeyDto req);

    IPage<WorkflowRunTaskDto> getWorkflowRunTaskList(WorkflowRunKeyDto req);

    WorkflowVersionKeyDto createWorkflowOfWizardMode(String workflowName, String workflowDesc, Long algorithmId, Long calculationProcessId);

    WorkflowVersionKeyDto createWorkflowOfExpertMode(String workflowName);
}
