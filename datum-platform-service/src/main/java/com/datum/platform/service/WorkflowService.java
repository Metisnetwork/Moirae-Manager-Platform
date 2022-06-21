package com.datum.platform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.datum.platform.mapper.domain.*;
import com.datum.platform.service.dto.task.TaskEventDto;
import com.datum.platform.service.dto.task.TaskResultDto;
import com.datum.platform.service.dto.workflow.*;
import com.datum.platform.service.dto.workflow.expert.WorkflowDetailsOfExpertModeDto;
import com.datum.platform.service.dto.workflow.expert.WorkflowNodeKeyDto;
import com.datum.platform.service.dto.workflow.expert.WorkflowStatusOfExpertModeDto;
import com.datum.platform.service.dto.workflow.wizard.WorkflowDetailsOfWizardModeDto;
import com.datum.platform.service.dto.workflow.wizard.WorkflowWizardStepDto;

import java.util.Date;
import java.util.List;

public interface WorkflowService{

    int getWorkflowCount();

    IPage<Workflow> getWorkflowList(Long current, Long size, String keyword, Long algorithmId, Date begin, Date end, Integer createMode);

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

    WorkflowFeeDto preparationStart(WorkflowVersionKeyDto req);

    List<WorkflowRunTaskDto> getWorkflowRunTaskList(WorkflowRunKeyDto req);

    WorkflowRunTaskResultDto getWorkflowRunTaskResult(String taskId);

    WorkflowVersionKeyDto createWorkflowOfWizardMode(String workflowName, String workflowDesc, Long algorithmId, Long calculationProcessId);

    WorkflowVersionKeyDto createWorkflowOfExpertMode(String workflowName);

    /**
     * 查询待确认的任务列表
     *
     * @return
     */
    List<WorkflowRunTaskStatus> listWorkflowRunTaskStatusOfUnConfirmed();

    /**
     * 取消任务
     *
     * @param workflowRunTaskStatus
     * @return
     */
    boolean cancelWorkflowRunTaskStatus(WorkflowRunTaskStatus workflowRunTaskStatus);

    /**
     * 任务结束
     *
     * @param workflowRunTaskStatus
     * @param task
     */
    void taskFinish(WorkflowRunTaskStatus workflowRunTaskStatus, Task task);

    List<WorkflowStartCredentialDto> preparationStartCredentialList(WorkflowVersionKeyDto req);
}
