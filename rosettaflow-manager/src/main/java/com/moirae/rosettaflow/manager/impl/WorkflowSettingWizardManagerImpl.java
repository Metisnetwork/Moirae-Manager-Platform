package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.common.enums.OldAndNewEnum;
import com.moirae.rosettaflow.manager.WorkflowSettingWizardManager;
import com.moirae.rosettaflow.mapper.WorkflowSettingWizardMapper;
import com.moirae.rosettaflow.mapper.domain.WorkflowSettingWizard;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Override
    public List<Map<OldAndNewEnum, WorkflowSettingWizard>> copyAndReset(Long workflowId, Long oldVersion, Long newVersion) {
        List<WorkflowSettingWizard> oldList = listByWorkflowVersion(workflowId, oldVersion);
        List<Map<OldAndNewEnum, WorkflowSettingWizard>> result  = oldList.stream()
                .map(item -> {
                    WorkflowSettingWizard newObj =  new WorkflowSettingWizard();
                    newObj.setWorkflowId(item.getWorkflowId());
                    newObj.setWorkflowVersion(newVersion);
                    newObj.setStep(item.getStep());
                    newObj.setCalculationProcessStepType(item.getCalculationProcessStepType());
                    newObj.setTask1Step(item.getTask1Step());
                    newObj.setTask2Step(item.getTask2Step());
                    newObj.setTask3Step(item.getTask3Step());
                    newObj.setTask4Step(item.getTask4Step());
                    save(newObj);

                    Map<OldAndNewEnum, WorkflowSettingWizard> pair = new HashMap<>();
                    pair.put(OldAndNewEnum.OLD, item);
                    pair.put(OldAndNewEnum.NEW, newObj);
                    return pair;
                })
                .collect(Collectors.toList());
        return result;
    }

    @Override
    public List<WorkflowSettingWizard> deleteWorkflowId(Long workflowId) {
        LambdaQueryWrapper<WorkflowSettingWizard> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowSettingWizard::getWorkflowId, workflowId);
        List<WorkflowSettingWizard> result = list(wrapper);
        remove(wrapper);
        return result;
    }

    private List<WorkflowSettingWizard> listByWorkflowVersion(Long workflowId, Long workflowVersion) {
        LambdaQueryWrapper<WorkflowSettingWizard> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowSettingWizard::getWorkflowId, workflowId);
        wrapper.eq(WorkflowSettingWizard::getWorkflowVersion, workflowVersion);
        return list(wrapper);
    }
}
