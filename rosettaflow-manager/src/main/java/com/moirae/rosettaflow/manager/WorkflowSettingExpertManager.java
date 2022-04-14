package com.moirae.rosettaflow.manager;

import com.moirae.rosettaflow.common.enums.OldAndNewEnum;
import com.moirae.rosettaflow.mapper.domain.WorkflowSettingExpert;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.WorkflowTask;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工作流专家模式节点表 服务类
 * </p>
 *
 * @author chendai
 * @since 2022-03-30
 */
public interface WorkflowSettingExpertManager extends IService<WorkflowSettingExpert> {

    List<WorkflowSettingExpert> listByWorkflowVersion(Long workflowId, Long workflowVersion);

    boolean removeByWorkflowVersion(Long workflowId, Long workflowVersion);

    WorkflowSettingExpert getByWorkflowVersionAndStep(Long workflowId, Long workflowVersion, Integer nodeStep);

    List<Map<OldAndNewEnum, WorkflowSettingExpert>> copyAndReset(Long workflowId, Long oldVersion, Long newVersion);

    List<WorkflowSettingExpert> deleteWorkflowId(Long workflowId);
}
