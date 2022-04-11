package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.moirae.rosettaflow.mapper.domain.WorkflowTaskOutput;
import com.moirae.rosettaflow.mapper.WorkflowTaskOutputMapper;
import com.moirae.rosettaflow.manager.WorkflowTaskOutputManager;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 项目工作流节点输出表 服务实现类
 * </p>
 *
 * @author chendai
 * @since 2022-03-30
 */
@Service
public class WorkflowTaskOutputManagerImpl extends ServiceImpl<WorkflowTaskOutputMapper, WorkflowTaskOutput> implements WorkflowTaskOutputManager {

    @Override
    public List<WorkflowTaskOutput> listByWorkflowTaskId(Long workflowTaskId) {
        LambdaQueryWrapper<WorkflowTaskOutput> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowTaskOutput::getWorkflowTaskId, workflowTaskId);
        return list(wrapper);
    }

    @Override
    public void clearAndSave(Long workflowTaskId, List<WorkflowTaskOutput> workflowTaskOutputList) {
        clear(workflowTaskId);
        saveBatch(workflowTaskOutputList);
    }


    private boolean clear(Long workflowTaskId){
        LambdaQueryWrapper<WorkflowTaskOutput> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowTaskOutput::getWorkflowTaskId, workflowTaskId);
        return remove(wrapper);
    }
}
