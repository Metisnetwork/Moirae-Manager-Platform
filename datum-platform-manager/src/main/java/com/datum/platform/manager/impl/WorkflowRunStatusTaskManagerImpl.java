package com.datum.platform.manager.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datum.platform.manager.WorkflowRunStatusTaskManager;
import com.datum.platform.mapper.WorkflowRunStatusTaskMapper;
import com.datum.platform.mapper.domain.WorkflowRunStatusTask;
import com.datum.platform.mapper.enums.WorkflowTaskRunStatusEnum;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 工作流任务运行状态 服务实现类
 * </p>
 *
 * @author chendai
 * @since 2022-03-30
 */
@Service
public class WorkflowRunStatusTaskManagerImpl extends ServiceImpl<WorkflowRunStatusTaskMapper, WorkflowRunStatusTask> implements WorkflowRunStatusTaskManager {

    @Override
    public WorkflowRunStatusTask getByWorkflowRunIdAndStep(Long workflowRunId, Integer taskStep) {
        LambdaQueryWrapper<WorkflowRunStatusTask> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowRunStatusTask::getWorkflowRunId, workflowRunId);
        wrapper.eq(WorkflowRunStatusTask::getStep, taskStep);
        return getOne(wrapper);
    }

    @Override
    public List<WorkflowRunStatusTask> listByWorkflowRunIdAndHasTaskId(Long workflowRunId) {
        LambdaQueryWrapper<WorkflowRunStatusTask> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowRunStatusTask::getWorkflowTaskId, workflowRunId);
        wrapper.isNotNull(WorkflowRunStatusTask::getTaskId);
        wrapper.orderByAsc(WorkflowRunStatusTask::getStep);
        return list(wrapper);
    }

    @Override
    public List<WorkflowRunStatusTask> listOfUnConfirmed() {
        LambdaQueryWrapper<WorkflowRunStatusTask> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowRunStatusTask::getRunStatus, WorkflowTaskRunStatusEnum.RUN_DOING);
        wrapper.isNotNull(WorkflowRunStatusTask::getTaskId);
        return list(wrapper);
    }

    @Override
    public List<WorkflowRunStatusTask> listOfUncompleted(long workflowRunId) {
        LambdaQueryWrapper<WorkflowRunStatusTask> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowRunStatusTask::getRunStatus, WorkflowTaskRunStatusEnum.RUN_NEED);
        wrapper.eq(WorkflowRunStatusTask::getWorkflowRunId,workflowRunId);
        return list(wrapper);
    }

    @Override
    public List<WorkflowRunStatusTask> listByWorkflowRunId(Long workflowRunId) {
        LambdaQueryWrapper<WorkflowRunStatusTask> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowRunStatusTask::getWorkflowRunId, workflowRunId);
        wrapper.orderByAsc(WorkflowRunStatusTask::getStep);
        return list(wrapper);
    }

    @Override
    public WorkflowRunStatusTask getByTaskId(String taskId) {
        LambdaQueryWrapper<WorkflowRunStatusTask> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowRunStatusTask::getTaskId, taskId);
        return getOne(wrapper);
    }

    @Override
    public WorkflowRunStatusTask getByTaskName(String taskName) {
        LambdaQueryWrapper<WorkflowRunStatusTask> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowRunStatusTask::getTaskName, taskName);
        return getOne(wrapper);
    }

    @Override
    public boolean updateByTaskName(String taskName) {
        if(StrUtil.isBlank(taskName)){
            return false;
        }
        LambdaUpdateWrapper<WorkflowRunStatusTask> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(WorkflowRunStatusTask::getTaskName, taskName);
        boolean update = update(wrapper);
        return update;
    }
}
