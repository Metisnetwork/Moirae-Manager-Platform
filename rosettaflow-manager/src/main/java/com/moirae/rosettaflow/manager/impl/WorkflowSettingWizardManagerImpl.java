package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.moirae.rosettaflow.mapper.domain.WorkflowSettingWizard;
import com.moirae.rosettaflow.mapper.WorkflowSettingWizardMapper;
import com.moirae.rosettaflow.manager.WorkflowSettingWizardManager;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 工作流向导模式步骤表 服务实现类
 * </p>
 *
 * @author chendai
 * @since 2022-03-30
 */
@Service
public class WorkflowSettingWizardManagerImpl extends ServiceImpl<WorkflowSettingWizardMapper, WorkflowSettingWizard> implements WorkflowSettingWizardManager {

    @Override
    public WorkflowSettingWizard getOneByStep(Long workflowId, Long workflowVersion, Integer step) {
        LambdaQueryWrapper<WorkflowSettingWizard> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowSettingWizard::getWorkflowId, workflowId);
        wrapper.eq(WorkflowSettingWizard::getWorkflowVersion, workflowVersion);
        wrapper.eq(WorkflowSettingWizard::getStep, step);
        return getOne(wrapper);
    }
}
