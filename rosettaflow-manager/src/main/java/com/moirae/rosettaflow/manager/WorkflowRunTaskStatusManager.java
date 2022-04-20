package com.moirae.rosettaflow.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.WorkflowRunTaskStatus;

import java.util.List;

/**
 * <p>
 * 工作流任务运行状态 服务类
 * </p>
 *
 * @author chendai
 * @since 2022-03-30
 */
public interface WorkflowRunTaskStatusManager extends IService<WorkflowRunTaskStatus> {

    WorkflowRunTaskStatus getByWorkflowRunIdAndStep(Long workflowRunId, Integer taskStep);

    List<WorkflowRunTaskStatus> listByWorkflowRunIdAndHasTaskId(Long workflowRunId);

    List<WorkflowRunTaskStatus> listOfUnConfirmed();

    List<WorkflowRunTaskStatus> listByWorkflowRunId(Long workflowRunId);
}
