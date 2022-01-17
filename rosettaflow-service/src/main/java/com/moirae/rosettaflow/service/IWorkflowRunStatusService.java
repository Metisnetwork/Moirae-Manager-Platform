package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.dto.WorkflowDto;
import com.moirae.rosettaflow.grpc.task.req.dto.TaskDto;
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

    TaskDto assemblyTaskDto(WorkflowDto workflowDto);

    WorkflowRunStatus submitTaskAndExecute(Long workflowId, Integer version, String address, String sign);

    List<WorkflowRunTaskStatus> queryUnConfirmedWorkflowRunTaskStatus();

    void taskFinish(Long aLong, String taskId, int state, long taskStartAt, long taskEndAt);
}
