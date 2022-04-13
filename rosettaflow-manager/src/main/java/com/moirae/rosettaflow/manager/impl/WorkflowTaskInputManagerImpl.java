package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.moirae.rosettaflow.mapper.domain.WorkflowTaskInput;
import com.moirae.rosettaflow.mapper.WorkflowTaskInputMapper;
import com.moirae.rosettaflow.manager.WorkflowTaskInputManager;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 工作流任务输入表 服务实现类
 * </p>
 *
 * @author chendai
 * @since 2022-03-30
 */
@Service
public class WorkflowTaskInputManagerImpl extends ServiceImpl<WorkflowTaskInputMapper, WorkflowTaskInput> implements WorkflowTaskInputManager {

    @Override
    public List<WorkflowTaskInput> listByWorkflowTaskId(Long workflowTaskId) {
        LambdaQueryWrapper<WorkflowTaskInput> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowTaskInput::getWorkflowTaskId, workflowTaskId);
        return list(wrapper);
    }

    @Override
    public void clearAndSave(Long workflowTaskId, List<WorkflowTaskInput> trainingWorkflowTaskInputList) {
        removeByWorkflowTaskId(workflowTaskId);
        saveBatch(trainingWorkflowTaskInputList);
    }

    @Override
    public boolean removeByWorkflowTaskIds(List<Long> workflowTaskIdList) {
        LambdaQueryWrapper<WorkflowTaskInput> wrapper = Wrappers.lambdaQuery();
        wrapper.in(WorkflowTaskInput::getWorkflowTaskId, workflowTaskIdList);
        return remove(wrapper);
    }

    private boolean removeByWorkflowTaskId(Long workflowTaskId){
        LambdaQueryWrapper<WorkflowTaskInput> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowTaskInput::getWorkflowTaskId, workflowTaskId);
        return remove(wrapper);
    }
}
