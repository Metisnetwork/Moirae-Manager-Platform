package com.moirae.rosettaflow.manager;

import com.moirae.rosettaflow.common.enums.OldAndNewEnum;
import com.moirae.rosettaflow.mapper.domain.WorkflowTask;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工作流任务配置表 服务类
 * </p>
 *
 * @author chendai
 * @since 2022-03-30
 */
public interface WorkflowTaskManager extends IService<WorkflowTask> {

    WorkflowTask getByStep(Long workflowId, Long workflowVersion, Integer task1Step);

    List<WorkflowTask> listByWorkflowVersion(Long workflowId, Long workflowVersion);

    List<Map<OldAndNewEnum, WorkflowTask>> copy(Long workflowId, Long oldVersion, Long newVersion);

    List<WorkflowTask> deleteWorkflowId(Long workflowId);
}
