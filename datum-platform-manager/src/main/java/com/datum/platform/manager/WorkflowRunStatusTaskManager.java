package com.datum.platform.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.datum.platform.mapper.domain.WorkflowRunStatusTask;

import java.util.List;

/**
 * <p>
 * 工作流任务运行状态 服务类
 * </p>
 *
 * @author chendai
 * @since 2022-03-30
 */
public interface WorkflowRunStatusTaskManager extends IService<WorkflowRunStatusTask> {

    WorkflowRunStatusTask getByWorkflowRunIdAndStep(Long workflowRunId, Integer taskStep);

    List<WorkflowRunStatusTask> listByWorkflowRunIdAndHasTaskId(Long workflowRunId);

    List<WorkflowRunStatusTask> listOfUnConfirmed();

    List<WorkflowRunStatusTask> listOfUncompleted(long workflowRunId);

    List<WorkflowRunStatusTask> listByWorkflowRunId(Long workflowRunId);

    WorkflowRunStatusTask getByTaskId(String taskId);

    WorkflowRunStatusTask getByTaskName(String taskName);

    boolean updateByTaskName(String taskName);
}
