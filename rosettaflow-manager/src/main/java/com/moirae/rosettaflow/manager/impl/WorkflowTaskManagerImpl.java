package com.moirae.rosettaflow.manager.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.common.enums.OldAndNewEnum;
import com.moirae.rosettaflow.manager.WorkflowTaskManager;
import com.moirae.rosettaflow.mapper.WorkflowTaskMapper;
import com.moirae.rosettaflow.mapper.domain.WorkflowSettingWizard;
import com.moirae.rosettaflow.mapper.domain.WorkflowTask;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 工作流任务配置表 服务实现类
 * </p>
 *
 * @author chendai
 * @since 2022-03-30
 */
@Service
public class WorkflowTaskManagerImpl extends ServiceImpl<WorkflowTaskMapper, WorkflowTask> implements WorkflowTaskManager {

    @Override
    public WorkflowTask getByStep(Long workflowId, Long workflowVersion, Integer task1Step) {
        LambdaQueryWrapper<WorkflowTask> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowTask::getWorkflowId, workflowId);
        wrapper.eq(WorkflowTask::getWorkflowVersion, workflowVersion);
        wrapper.eq(WorkflowTask::getStep, task1Step);
        return getOne(wrapper);
    }

    @Override
    public List<WorkflowTask> listByWorkflowVersion(Long workflowId, Long workflowVersion) {
        LambdaQueryWrapper<WorkflowTask> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowTask::getWorkflowId, workflowId);
        wrapper.eq(WorkflowTask::getWorkflowVersion, workflowVersion);
        return list(wrapper);
    }

    @Override
    public List<Map<OldAndNewEnum, WorkflowTask>> copy(Long workflowId, Long oldVersion, Long newVersion) {
        List<WorkflowTask> oldList = listByWorkflowVersion(workflowId, oldVersion);
        List<Map<OldAndNewEnum, WorkflowTask>> result  = oldList.stream()
                .map(item -> {
                    WorkflowTask newObj =  new WorkflowTask();
                    newObj.setWorkflowId(item.getWorkflowId());
                    newObj.setWorkflowVersion(newVersion);
                    newObj.setStep(item.getStep());
                    newObj.setAlgorithmId(item.getAlgorithmId());
                    newObj.setIdentityId(item.getIdentityId());
                    newObj.setInputModel(item.getInputModel());
                    newObj.setInputModelId(item.getInputModelId());
                    newObj.setInputPsi(item.getInputPsi());
                    newObj.setInputPsiId(item.getInputPsiId());
                    save(newObj);

                    Map<OldAndNewEnum, WorkflowTask> pair = new HashMap<>();
                    pair.put(OldAndNewEnum.OLD, item);
                    pair.put(OldAndNewEnum.NEW, newObj);
                    return pair;
                })
                .collect(Collectors.toList());
        return result;
    }

    @Override
    public List<WorkflowTask> deleteWorkflowId(Long workflowId) {
        LambdaQueryWrapper<WorkflowTask> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowTask::getWorkflowId, workflowId);
        List<WorkflowTask> result = list(wrapper);
        if(result.size() > 0){
            remove(wrapper);
        }
        return result;
    }

    @Override
    public List<WorkflowTask> listExecutableByWorkflowVersion(Long workflowId, Long workflowVersion) {
        List<WorkflowTask> list = listByWorkflowVersion(workflowId, workflowVersion);
        Set<Integer> enableSet = list.stream().map(WorkflowTask::getStep).collect(Collectors.toSet());
        list.forEach(item -> {
            if(!item.getInputPsi() &&  item.getStep() - 1 >= 1){
                enableSet.remove(item.getStep() - 1);
            }
        });
        return null;
    }
}
