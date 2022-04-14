package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.moirae.rosettaflow.mapper.domain.WorkflowSettingExpert;
import com.moirae.rosettaflow.mapper.WorkflowSettingExpertMapper;
import com.moirae.rosettaflow.manager.WorkflowSettingExpertManager;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
