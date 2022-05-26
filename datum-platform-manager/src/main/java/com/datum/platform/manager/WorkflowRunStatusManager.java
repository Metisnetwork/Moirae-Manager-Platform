package com.datum.platform.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.datum.platform.mapper.domain.WorkflowRunStatus;

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

    boolean hasBeenRun(Long workflowId);
}
