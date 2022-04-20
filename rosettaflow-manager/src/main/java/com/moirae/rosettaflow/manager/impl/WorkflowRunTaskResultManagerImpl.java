package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.manager.WorkflowRunTaskResultManager;
import com.moirae.rosettaflow.mapper.WorkflowRunTaskResultMapper;
import com.moirae.rosettaflow.mapper.domain.WorkflowRunTaskResult;
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
public class WorkflowRunTaskResultManagerImpl extends ServiceImpl<WorkflowRunTaskResultMapper, WorkflowRunTaskResult> implements WorkflowRunTaskResultManager {

    @Override
    public List<WorkflowRunTaskResult> listByTaskId(String taskId) {
        LambdaQueryWrapper<WorkflowRunTaskResult> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowRunTaskResult::getTaskId, taskId);
        return list(wrapper);
    }
}
