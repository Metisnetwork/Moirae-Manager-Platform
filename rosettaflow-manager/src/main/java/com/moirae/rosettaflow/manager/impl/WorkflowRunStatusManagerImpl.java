package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.manager.WorkflowRunStatusManager;
import com.moirae.rosettaflow.mapper.WorkflowRunStatusMapper;
import com.moirae.rosettaflow.mapper.domain.WorkflowRunStatus;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 工作流运行状态 服务实现类
 * </p>
 *
 * @author chendai
 * @since 2022-03-30
 */
@Service
public class WorkflowRunStatusManagerImpl extends ServiceImpl<WorkflowRunStatusMapper, WorkflowRunStatus> implements WorkflowRunStatusManager {

    @Override
    public WorkflowRunStatus getLatestOneByWorkflowVersion(Long workflowId, Long workflowVersion) {
        LambdaQueryWrapper<WorkflowRunStatus> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowRunStatus::getWorkflowId, workflowId);
        wrapper.eq(WorkflowRunStatus::getWorkflowVersion, workflowVersion);
        wrapper.orderByDesc(WorkflowRunStatus::getId);
        wrapper.last("limit 1");
        return getOne(wrapper);
    }

    @Override
    public boolean hasBeenRun(Long workflowId) {
        LambdaQueryWrapper<WorkflowRunStatus> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowRunStatus::getWorkflowId, workflowId);
        return count(wrapper) > 0;
    }
}
