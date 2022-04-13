package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.moirae.rosettaflow.mapper.domain.WorkflowTaskVariable;
import com.moirae.rosettaflow.mapper.WorkflowTaskVariableMapper;
import com.moirae.rosettaflow.manager.WorkflowTaskVariableManager;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 工作流任务变量表 服务实现类
 * </p>
 *
 * @author chendai
 * @since 2022-03-30
 */
@Service
public class WorkflowTaskVariableManagerImpl extends ServiceImpl<WorkflowTaskVariableMapper, WorkflowTaskVariable> implements WorkflowTaskVariableManager {

    @Override
    public List<WorkflowTaskVariable> listByWorkflowTaskId(Long workflowTaskId) {
        LambdaQueryWrapper<WorkflowTaskVariable> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowTaskVariable::getWorkflowTaskId, workflowTaskId);
        return list(wrapper);
    }

    @Override
    public boolean removeByWorkflowTaskIds(List<Long> workflowTaskIdList) {
        LambdaQueryWrapper<WorkflowTaskVariable> wrapper = Wrappers.lambdaQuery();
        wrapper.in(WorkflowTaskVariable::getWorkflowTaskId, workflowTaskIdList);
        return remove(wrapper);
    }
}
