package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.common.enums.OldAndNewEnum;
import com.moirae.rosettaflow.manager.WorkflowTaskOutputManager;
import com.moirae.rosettaflow.mapper.WorkflowTaskOutputMapper;
import com.moirae.rosettaflow.mapper.domain.WorkflowTaskOutput;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        removeByWorkflowTaskId(workflowTaskId);
        saveBatch(workflowTaskOutputList);
    }

    @Override
    public boolean removeByWorkflowTaskIds(List<Long> workflowTaskIdList) {
        LambdaQueryWrapper<WorkflowTaskOutput> wrapper = Wrappers.lambdaQuery();
        wrapper.in(WorkflowTaskOutput::getWorkflowTaskId, workflowTaskIdList);
        return remove(wrapper);
    }

    @Override
    public List<Map<OldAndNewEnum, WorkflowTaskOutput>> copy(Long oldWorkflowTaskId, Long newWorkflowTaskId) {
        List<WorkflowTaskOutput> oldList = listByWorkflowTaskId(oldWorkflowTaskId);
        List<Map<OldAndNewEnum, WorkflowTaskOutput>> result  = oldList.stream()
                .map(item -> {
                    WorkflowTaskOutput newObj =  new WorkflowTaskOutput();
                    newObj.setWorkflowTaskId(newWorkflowTaskId);
                    newObj.setIdentityId(item.getIdentityId());
                    newObj.setStorePattern(item.getStorePattern());
                    newObj.setOutputContent(item.getOutputContent());
                    newObj.setPartyId(item.getPartyId());
                    save(newObj);

                    Map<OldAndNewEnum, WorkflowTaskOutput> pair = new HashMap<>();
                    pair.put(OldAndNewEnum.OLD, item);
                    pair.put(OldAndNewEnum.NEW, newObj);
                    return pair;
                })
                .collect(Collectors.toList());
        return result;
    }

    @Override
    public List<WorkflowTaskOutput> deleteByWorkflowTaskId(Long workflowTaskId) {
        LambdaQueryWrapper<WorkflowTaskOutput> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowTaskOutput::getWorkflowTaskId, workflowTaskId);
        List<WorkflowTaskOutput> result = list(wrapper);
        if(result.size() > 0){
            remove(wrapper);
        }
        return result;
    }

    @Override
    public boolean setWorkflowTaskOutput(Long workflowTaskId, List<WorkflowTaskOutput> workflowTaskOutputList) {
        removeByWorkflowTaskId(workflowTaskId);
        return saveBatch(workflowTaskOutputList);
    }


    private boolean removeByWorkflowTaskId(Long workflowTaskId){
        LambdaQueryWrapper<WorkflowTaskOutput> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowTaskOutput::getWorkflowTaskId, workflowTaskId);
        return remove(wrapper);
    }
}
