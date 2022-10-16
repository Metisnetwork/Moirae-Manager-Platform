package com.datum.platform.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datum.platform.manager.WorkflowRunStatusManager;
import com.datum.platform.mapper.WorkflowRunStatusMapper;
import com.datum.platform.mapper.domain.WorkflowRunStatus;
import com.datum.platform.mapper.enums.WorkflowTaskRunStatusEnum;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<WorkflowRunStatus> getRunningWorkflow() {
        LambdaQueryWrapper<WorkflowRunStatus> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowRunStatus::getRunStatus, WorkflowTaskRunStatusEnum.RUN_DOING);
        return list(wrapper);
    }
}
