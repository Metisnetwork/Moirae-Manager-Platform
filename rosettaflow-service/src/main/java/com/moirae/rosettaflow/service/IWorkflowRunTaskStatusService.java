package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.WorkflowRunTaskStatus;

import java.util.List;

public interface IWorkflowRunTaskStatusService extends IService<WorkflowRunTaskStatus> {

    List<WorkflowRunTaskStatus> listByWorkflowRunStatusId(Long workflowRunStatusId);

    List<WorkflowRunTaskStatus> queryUnConfirmedWorkflowRunTaskStatus();
}
