package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.WorkflowRunTaskResult;

import java.util.List;

public interface IWorkflowRunTaskResultService extends IService<WorkflowRunTaskResult> {
    List<WorkflowRunTaskResult> queryByTaskId(String taskId);
}
