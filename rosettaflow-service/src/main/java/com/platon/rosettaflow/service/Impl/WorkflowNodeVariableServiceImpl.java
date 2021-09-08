package com.platon.rosettaflow.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.mapper.WorkflowNodeVariableMapper;
import com.platon.rosettaflow.mapper.domain.AlgorithmVariable;
import com.platon.rosettaflow.mapper.domain.WorkflowNodeVariable;
import com.platon.rosettaflow.service.IWorkflowNodeVariableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/18
 * @description 工作流节点变量服务实现类
 */
@Slf4j
@Service
public class WorkflowNodeVariableServiceImpl extends ServiceImpl<WorkflowNodeVariableMapper, WorkflowNodeVariable> implements IWorkflowNodeVariableService {
    @Override
    public List<WorkflowNodeVariable> getByWorkflowNodeId(Long workflowNodeId) {
        LambdaQueryWrapper<WorkflowNodeVariable> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowNodeVariable::getWorkflowNodeId, workflowNodeId);
        wrapper.eq(WorkflowNodeVariable::getStatus, 1);
        return this.list(wrapper);
    }

    @Override
    public void deleteByWorkflowNodeId(Long workflowNodeId) {
        LambdaQueryWrapper<WorkflowNodeVariable> delWrapper = Wrappers.lambdaQuery();
        delWrapper.eq(WorkflowNodeVariable::getWorkflowNodeId, workflowNodeId);
        this.remove(delWrapper);
    }
    @Override
    public void deleteLogicByWorkflowNodeId(Long workflowNodeId) {
        LambdaUpdateWrapper<WorkflowNodeVariable> delWrapper = Wrappers.lambdaUpdate();
        delWrapper.eq(WorkflowNodeVariable::getWorkflowNodeId, workflowNodeId);
        delWrapper.set(WorkflowNodeVariable::getStatus, 0);
        this.update(delWrapper);
    }

    @Override
    public void addByAlgorithmVariableList(Long workflowNodeId, List<AlgorithmVariable> algorithmVariableList) {
        WorkflowNodeVariable workflowNodeVariable;
        List<WorkflowNodeVariable> workflowNodeVariableList = new ArrayList<>();
        for (AlgorithmVariable algorithmVariable : algorithmVariableList) {
            workflowNodeVariable = new WorkflowNodeVariable();
            workflowNodeVariable.setWorkflowNodeId(workflowNodeId);
            workflowNodeVariable.setVarNodeType(algorithmVariable.getVarType());
            workflowNodeVariable.setVarNodeKey(algorithmVariable.getVarKey());
            workflowNodeVariable.setVarNodeValue(algorithmVariable.getVarValue());
            workflowNodeVariable.setVarNodeDesc(algorithmVariable.getVarDesc());
            workflowNodeVariableList.add(workflowNodeVariable);
        }
        this.saveBatch(workflowNodeVariableList);
    }
}
