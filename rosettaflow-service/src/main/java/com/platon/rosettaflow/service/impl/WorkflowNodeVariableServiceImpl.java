package com.platon.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.common.enums.StatusEnum;
import com.platon.rosettaflow.mapper.WorkflowNodeVariableMapper;
import com.platon.rosettaflow.mapper.domain.AlgorithmVariable;
import com.platon.rosettaflow.mapper.domain.WorkflowNodeResource;
import com.platon.rosettaflow.mapper.domain.WorkflowNodeVariable;
import com.platon.rosettaflow.service.IWorkflowNodeVariableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 工作流节点变量服务实现类
 * @author hudenian
 * @date 2021/8/18
 */
@Slf4j
@Service
public class WorkflowNodeVariableServiceImpl extends ServiceImpl<WorkflowNodeVariableMapper, WorkflowNodeVariable> implements IWorkflowNodeVariableService {
    @Override
    public List<WorkflowNodeVariable> getByWorkflowNodeId(Long workflowNodeId) {
        LambdaQueryWrapper<WorkflowNodeVariable> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowNodeVariable::getWorkflowNodeId, workflowNodeId);
        wrapper.eq(WorkflowNodeVariable::getStatus, StatusEnum.VALID.getValue());
        return this.list(wrapper);
    }

    @Override
    public void deleteByWorkflowNodeId(List<Long> workflowNodeIdList) {
        LambdaQueryWrapper<WorkflowNodeVariable> delWrapper = Wrappers.lambdaQuery();
        delWrapper.in(WorkflowNodeVariable::getWorkflowNodeId, workflowNodeIdList);
        this.remove(delWrapper);
    }
    @Override
    public void deleteLogicByWorkflowNodeId(Long workflowNodeId) {
        LambdaUpdateWrapper<WorkflowNodeVariable> delWrapper = Wrappers.lambdaUpdate();
        delWrapper.eq(WorkflowNodeVariable::getWorkflowNodeId, workflowNodeId);
        delWrapper.set(WorkflowNodeVariable::getStatus, StatusEnum.UN_VALID.getValue());
        this.update(delWrapper);
    }

    @Override
    public List<WorkflowNodeVariable> copyWorkflowNodeVariable(Long newNodeId, Long oldNodeId) {
        List<WorkflowNodeVariable> oldNodeVariableList = this.getByWorkflowNodeId(oldNodeId);
        if (oldNodeVariableList.size() == 0) {
            return null;
        }
        List<WorkflowNodeVariable> newNodeVariableList = new ArrayList<>();
        oldNodeVariableList.forEach(oldNodeVariable -> {
            WorkflowNodeVariable newNodeVariable = new WorkflowNodeVariable();
            newNodeVariable.setWorkflowNodeId(newNodeId);
            newNodeVariable.setVarNodeKey(oldNodeVariable.getVarNodeKey());
            newNodeVariable.setVarNodeValue(oldNodeVariable.getVarNodeValue());
            newNodeVariable.setVarNodeDesc(oldNodeVariable.getVarNodeDesc());
            newNodeVariable.setVarNodeType(oldNodeVariable.getVarNodeType());
            newNodeVariableList.add(newNodeVariable);
        });

        return newNodeVariableList;
    }

    @Override
    public void batchInsert(List<WorkflowNodeVariable> workflowNodeVariableList) {
        this.baseMapper.batchInsert(workflowNodeVariableList);
    }

    @Override
    public void addByAlgorithmVariableList(Long workflowNodeId, List<AlgorithmVariable> algorithmVariableList) {
        List<WorkflowNodeVariable> workflowNodeVariableList = new ArrayList<>();
        for (AlgorithmVariable algorithmVariable : algorithmVariableList) {
            WorkflowNodeVariable workflowNodeVariable = new WorkflowNodeVariable();
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
