package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.common.enums.OldAndNewEnum;
import com.moirae.rosettaflow.manager.WorkflowSettingExpertManager;
import com.moirae.rosettaflow.mapper.WorkflowSettingExpertMapper;
import com.moirae.rosettaflow.mapper.domain.WorkflowSettingExpert;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 工作流专家模式节点表 服务实现类
 * </p>
 *
 * @author chendai
 * @since 2022-03-30
 */
@Service
public class WorkflowSettingExpertManagerImpl extends ServiceImpl<WorkflowSettingExpertMapper, WorkflowSettingExpert> implements WorkflowSettingExpertManager {

    @Override
    public List<WorkflowSettingExpert> listByWorkflowVersion(Long workflowId, Long workflowVersion) {
        LambdaQueryWrapper<WorkflowSettingExpert> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowSettingExpert::getWorkflowId, workflowId);
        wrapper.eq(WorkflowSettingExpert::getWorkflowVersion, workflowVersion);
        return list(wrapper);
    }

    @Override
    public boolean removeByWorkflowVersion(Long workflowId, Long workflowVersion) {
        LambdaQueryWrapper<WorkflowSettingExpert> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowSettingExpert::getWorkflowId, workflowId);
        wrapper.eq(WorkflowSettingExpert::getWorkflowVersion, workflowVersion);
        return remove(wrapper);
    }

    @Override
    public WorkflowSettingExpert getByWorkflowVersionAndStep(Long workflowId, Long workflowVersion, Integer nodeStep) {
        LambdaQueryWrapper<WorkflowSettingExpert> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowSettingExpert::getWorkflowId, workflowId);
        wrapper.eq(WorkflowSettingExpert::getWorkflowVersion, workflowVersion);
        wrapper.eq(WorkflowSettingExpert::getNodeStep, nodeStep);
        return getOne(wrapper);
    }

    @Override
    public List<Map<OldAndNewEnum, WorkflowSettingExpert>> copyAndReset(Long workflowId, Long oldVersion, Long newVersion) {
        List<WorkflowSettingExpert> oldList = listByWorkflowVersion(workflowId, oldVersion);
        List<Map<OldAndNewEnum, WorkflowSettingExpert>> result = oldList.stream()
                .map(item -> {
                    WorkflowSettingExpert newObj =  new WorkflowSettingExpert();
                    newObj.setWorkflowId(item.getWorkflowId());
                    newObj.setWorkflowVersion(newVersion);
                    newObj.setNodeName(item.getNodeName());
                    newObj.setNodeStep(item.getNodeStep());
                    newObj.setPsiTaskStep(item.getPsiTaskStep());
                    newObj.setTaskStep(item.getTaskStep());
                    save(newObj);

                    Map<OldAndNewEnum, WorkflowSettingExpert> pair = new HashMap<>();
                    pair.put(OldAndNewEnum.OLD, item);
                    pair.put(OldAndNewEnum.NEW, newObj);
                    return pair;
                })
                .collect(Collectors.toList());
        return result;
    }

    @Override
    public List<WorkflowSettingExpert> deleteWorkflowId(Long workflowId) {
        LambdaQueryWrapper<WorkflowSettingExpert> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowSettingExpert::getWorkflowId, workflowId);
        List<WorkflowSettingExpert> result = list(wrapper);
        remove(wrapper);
        return result;
    }
}
