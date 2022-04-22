package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.common.enums.OldAndNewEnum;
import com.moirae.rosettaflow.manager.WorkflowTaskVariableManager;
import com.moirae.rosettaflow.mapper.WorkflowTaskVariableMapper;
import com.moirae.rosettaflow.mapper.domain.AlgorithmVariable;
import com.moirae.rosettaflow.mapper.domain.WorkflowTaskVariable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Override
    public List<Map<OldAndNewEnum, WorkflowTaskVariable>> copy(Long oldWorkflowTaskId, Long newWorkflowTaskId) {
        List<WorkflowTaskVariable> oldList = listByWorkflowTaskId(oldWorkflowTaskId);
        List<Map<OldAndNewEnum, WorkflowTaskVariable>> result  = oldList.stream()
                .map(item -> {
                    WorkflowTaskVariable newObj =  new WorkflowTaskVariable();
                    newObj.setWorkflowTaskId(newWorkflowTaskId);
                    newObj.setVarValue(item.getVarValue());
                    newObj.setVarKey(item.getVarKey());
                    newObj.setVarDesc(item.getVarDesc());
                    newObj.setVarDescEn(item.getVarDescEn());
                    save(newObj);
                    Map<OldAndNewEnum, WorkflowTaskVariable> pair = new HashMap<>();
                    pair.put(OldAndNewEnum.OLD, item);
                    pair.put(OldAndNewEnum.NEW, newObj);
                    return pair;
                })
                .collect(Collectors.toList());
        return result;
    }

    @Override
    public List<WorkflowTaskVariable> deleteByWorkflowTaskId(Long workflowTaskId) {
        LambdaQueryWrapper<WorkflowTaskVariable> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowTaskVariable::getWorkflowTaskId, workflowTaskId);
        List<WorkflowTaskVariable> result = list(wrapper);
        if(result.size() > 0){
            remove(wrapper);
        }
        return result;
    }

    @Override
    public boolean create(Long workflowTaskId, List<AlgorithmVariable> algorithmVariableList) {
        List<WorkflowTaskVariable> workflowTaskVariableList = algorithmVariableList.stream()
                .map(item -> {
                    WorkflowTaskVariable workflowTaskVariable = new WorkflowTaskVariable();
                    workflowTaskVariable.setWorkflowTaskId(workflowTaskId);
                    workflowTaskVariable.setVarKey(item.getVarKey());
                    workflowTaskVariable.setVarValue(item.getVarValue());
                    workflowTaskVariable.setVarDesc(item.getVarDesc());
                    workflowTaskVariable.setVarDescEn(item.getVarDescEn());
                    return workflowTaskVariable;
                })
                .collect(Collectors.toList());
       return saveBatch(workflowTaskVariableList);
    }
}
