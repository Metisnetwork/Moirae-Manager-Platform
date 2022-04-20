package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.common.enums.OldAndNewEnum;
import com.moirae.rosettaflow.manager.WorkflowTaskInputManager;
import com.moirae.rosettaflow.mapper.WorkflowTaskInputMapper;
import com.moirae.rosettaflow.mapper.domain.WorkflowTaskInput;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Override
    public List<Map<OldAndNewEnum, WorkflowTaskInput>> copy(Long oldWorkflowTaskId, Long newWorkflowTaskId) {
        List<WorkflowTaskInput> oldList = listByWorkflowTaskId(oldWorkflowTaskId);
        List<Map<OldAndNewEnum, WorkflowTaskInput>> result  = oldList.stream()
                .map(item -> {
                    WorkflowTaskInput newObj =  new WorkflowTaskInput();
                    newObj.setWorkflowTaskId(newWorkflowTaskId);
                    newObj.setMetaDataId(item.getMetaDataId());
                    newObj.setIdentityId(item.getIdentityId());
                    newObj.setKeyColumn(item.getKeyColumn());
                    newObj.setDependentVariable(item.getDependentVariable());
                    newObj.setDataColumnIds(item.getDataColumnIds());
                    newObj.setPartyId(item.getPartyId());
                    save(newObj);

                    Map<OldAndNewEnum, WorkflowTaskInput> pair = new HashMap<>();
                    pair.put(OldAndNewEnum.OLD, item);
                    pair.put(OldAndNewEnum.NEW, newObj);
                    return pair;
                })
                .collect(Collectors.toList());
        return result;
    }

    @Override
    public List<WorkflowTaskInput> deleteByWorkflowTaskId(Long workflowTaskId) {
        LambdaQueryWrapper<WorkflowTaskInput> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowTaskInput::getWorkflowTaskId, workflowTaskId);
        List<WorkflowTaskInput> result = list(wrapper);
        if(result.size() > 0){
            remove(wrapper);
        }
        return result;
    }

    private boolean removeByWorkflowTaskId(Long workflowTaskId){
        LambdaQueryWrapper<WorkflowTaskInput> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowTaskInput::getWorkflowTaskId, workflowTaskId);
        return remove(wrapper);
    }
}
