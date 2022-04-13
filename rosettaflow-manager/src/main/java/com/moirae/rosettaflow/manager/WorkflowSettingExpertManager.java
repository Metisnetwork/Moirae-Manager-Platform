package com.moirae.rosettaflow.manager;

import com.moirae.rosettaflow.mapper.domain.WorkflowSettingExpert;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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
}
