package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moirae.rosettaflow.mapper.domain.CalculationProcess;
import com.moirae.rosettaflow.mapper.domain.Workflow;
import com.moirae.rosettaflow.mapper.domain.WorkflowVersion;

import java.util.Date;
import java.util.List;

public interface WorkflowService{

    int getWorkflowCount();

    IPage<Workflow> getWorkflowList(Long current, Long size, String keyword, Long algorithmId, Date begin, Date end);

    IPage<WorkflowVersion> getWorkflowVersionList(Long current, Long size, Long workflowId);

    List<CalculationProcess> getCalculationProcessList(Long algorithmId);

    WorkflowVersion createWorkflowOfWizardMode(Workflow workflow);
}
