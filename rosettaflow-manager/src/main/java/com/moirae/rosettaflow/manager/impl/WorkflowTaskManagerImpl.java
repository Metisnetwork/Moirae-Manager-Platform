package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.moirae.rosettaflow.mapper.domain.WorkflowTask;
import com.moirae.rosettaflow.mapper.WorkflowTaskMapper;
import com.moirae.rosettaflow.manager.WorkflowTaskManager;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 工作流任务配置表 服务实现类
 * </p>
 *
 * @author chendai
 * @since 2022-03-30
 */
@Service
public class WorkflowTaskManagerImpl extends ServiceImpl<WorkflowTaskMapper, WorkflowTask> implements WorkflowTaskManager {

    @Override
    public WorkflowTask getByStep(Long workflowId, Long workflowVersion, Integer task1Step) {
        LambdaQueryWrapper<WorkflowTask> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowTask::getWorkflowId, workflowId);
        wrapper.eq(WorkflowTask::getWorkflowVersion, workflowVersion);
        wrapper.eq(WorkflowTask::getStep, task1Step);
        return getOne(wrapper);
    }
}
