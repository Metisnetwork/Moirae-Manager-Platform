package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.dto.WorkflowDto;
import com.moirae.rosettaflow.grpc.task.req.dto.TaskDto;
import com.moirae.rosettaflow.mapper.domain.ZOldWorkflowRunStatus;
import com.moirae.rosettaflow.mapper.domain.ZOldWorkflowRunTaskStatus;

import java.util.List;

/**
 * 工作流服务
 *
 * @author hudenian
 * @date 2021/8/16
 */
public interface ZOldIWorkflowRunStatusService extends IService<ZOldWorkflowRunStatus> {

    TaskDto assemblyTaskDto(WorkflowDto workflowDto);

    ZOldWorkflowRunStatus submitTaskAndExecute(Long workflowId, Integer version, String address, String sign);

    List<ZOldWorkflowRunTaskStatus> queryUnConfirmedWorkflowRunTaskStatus();

    void taskFinish(Long workflowRunStatusId, String taskId, int state, long taskStartAt, long taskEndAt);

    void updateCancelStatus(Long workflowRunStatusId, byte value);

    boolean cancel(ZOldWorkflowRunTaskStatus workflowRunTaskStatus);

    IPage<ZOldWorkflowRunStatus> runningRecordList(Long userId, Long projectId, String workflowName, IPage<ZOldWorkflowRunStatus> page);
}
