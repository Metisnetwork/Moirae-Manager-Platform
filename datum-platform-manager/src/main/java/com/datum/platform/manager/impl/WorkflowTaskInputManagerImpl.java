package com.datum.platform.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datum.platform.common.enums.OldAndNewEnum;
import com.datum.platform.manager.WorkflowTaskInputManager;
import com.datum.platform.mapper.WorkflowTaskInputMapper;
import com.datum.platform.mapper.domain.WorkflowTaskInput;
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
        wrapper.orderByAsc(WorkflowTaskInput::getSortKey);
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
                    newObj.setSortKey(item.getSortKey());
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

    @Override
    public boolean setWorkflowTaskInput(Long psiWorkflowTaskId, Long workflowTaskId, List<WorkflowTaskInput> workflowTaskInputList) {
        // 删除旧设置
        LambdaQueryWrapper<WorkflowTaskInput> wrapper = Wrappers.lambdaQuery();
        wrapper.in(WorkflowTaskInput::getWorkflowTaskId, psiWorkflowTaskId, workflowTaskId);
        remove(wrapper);
        // 组装PSI的设置
        List<WorkflowTaskInput> psiWorkflowTaskInputList = workflowTaskInputList.stream().map(
            item -> {
                WorkflowTaskInput psiWorkflowTaskInput = new WorkflowTaskInput();
                psiWorkflowTaskInput.setWorkflowTaskId(psiWorkflowTaskId);
                psiWorkflowTaskInput.setMetaDataId(item.getMetaDataId());
                psiWorkflowTaskInput.setIdentityId(item.getIdentityId());
                psiWorkflowTaskInput.setKeyColumn(item.getKeyColumn());
                psiWorkflowTaskInput.setPartyId(item.getPartyId());
                psiWorkflowTaskInput.setDataColumnIds(item.getDataColumnIds());
                psiWorkflowTaskInput.setDependentVariable(item.getDependentVariable());
                psiWorkflowTaskInput.setSortKey(item.getSortKey());
                return psiWorkflowTaskInput;
            }
        ).collect(Collectors.toList());
        psiWorkflowTaskInputList.addAll(workflowTaskInputList);
        return saveBatch(psiWorkflowTaskInputList);
    }

    private boolean removeByWorkflowTaskId(Long workflowTaskId){
        LambdaQueryWrapper<WorkflowTaskInput> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowTaskInput::getWorkflowTaskId, workflowTaskId);
        return remove(wrapper);
    }
}
