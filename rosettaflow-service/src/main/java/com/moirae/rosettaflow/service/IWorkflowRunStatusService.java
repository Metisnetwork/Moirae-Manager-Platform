package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.dto.WorkflowDto;
import com.moirae.rosettaflow.grpc.task.req.dto.TaskDto;
import com.moirae.rosettaflow.mapper.domain.Workflow;
import com.moirae.rosettaflow.mapper.domain.WorkflowRunStatus;
import com.moirae.rosettaflow.mapper.domain.WorkflowRunTaskStatus;

import java.util.List;

/**
 * 工作流服务
 *
 * @author hudenian
 * @date 2021/8/16
 */
public interface IWorkflowRunStatusService extends IService<WorkflowRunStatus> {

    /**
     * 启动工作流
     *
     * @param workflow
     * @return
     */
    WorkflowRunStatus submitTaskAndExecute(Workflow workflow);

    TaskDto assemblyTaskDto(WorkflowDto workflowDto);

    List<WorkflowRunTaskStatus> queryWorkflowRunTaskStatusByTaskId(String taskId);
}
