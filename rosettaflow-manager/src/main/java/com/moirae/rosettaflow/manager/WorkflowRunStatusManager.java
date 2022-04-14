package com.moirae.rosettaflow.manager;

import com.moirae.rosettaflow.mapper.domain.WorkflowRunStatus;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 工作流运行状态 服务类
 * </p>
 *
 * @author chendai
 * @since 2022-03-30
 */
public interface WorkflowRunStatusManager extends IService<WorkflowRunStatus> {

    WorkflowRunStatus getLatestOneByWorkflowVersion(Long workflowId, Long workflowVersion);
}
