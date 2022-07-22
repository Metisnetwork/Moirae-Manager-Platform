package com.datum.platform.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datum.platform.common.enums.OldAndNewEnum;
import com.datum.platform.common.enums.WorkflowTaskInputTypeEnum;
import com.datum.platform.manager.WorkflowTaskManager;
import com.datum.platform.mapper.WorkflowTaskMapper;
import com.datum.platform.mapper.domain.WorkflowTask;
import com.datum.platform.mapper.enums.WorkflowTaskPowerTypeEnum;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    public WorkflowTask getByStep(Long workflowId, Long workflowVersion, Integer taskStep) {
        LambdaQueryWrapper<WorkflowTask> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowTask::getWorkflowId, workflowId);
        wrapper.eq(WorkflowTask::getWorkflowVersion, workflowVersion);
        wrapper.eq(WorkflowTask::getStep, taskStep);
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
    public List<WorkflowTask> listByWorkflowVersionAndSteps(Long workflowId, Long workflowVersion, List<Integer> collect) {
        LambdaQueryWrapper<WorkflowTask> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowTask::getWorkflowId, workflowId);
        wrapper.eq(WorkflowTask::getWorkflowVersion, workflowVersion);
        wrapper.in(WorkflowTask::getStep, collect);
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
                    newObj.setInputModelStep(item.getInputModelStep());
                    newObj.setInputPsi(item.getInputPsi());
                    newObj.setInputPsiStep(item.getInputPsiStep());
                    newObj.setEnable(item.getEnable());
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
        LambdaQueryWrapper<WorkflowTask> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowTask::getWorkflowId, workflowId);
        wrapper.eq(WorkflowTask::getWorkflowVersion, workflowVersion);
        wrapper.eq(WorkflowTask::getEnable, true);
        return list(wrapper);
    }

    @Override
    public WorkflowTask createOfWizardMode(Long workflowId, Long workflowVersion, Integer step, Long algorithmId, Boolean inputModel, Integer inputModelStep, Boolean inputPsi, Integer inputPsiStep, Optional<WorkflowTaskPowerTypeEnum> powerType) {
        WorkflowTask workflowTask = new WorkflowTask();
        workflowTask.setWorkflowId(workflowId);
        workflowTask.setWorkflowVersion(workflowVersion);
        workflowTask.setStep(step);
        workflowTask.setAlgorithmId(algorithmId);
        workflowTask.setInputModel(inputModel);
        workflowTask.setInputModelStep(inputModelStep);
        workflowTask.setInputPsi(inputPsi);
        workflowTask.setInputPsiStep(inputPsiStep);
        powerType.ifPresent(powerTypeEnum -> {
            workflowTask.setPowerType(powerTypeEnum);
        });
        if(save(workflowTask)){
            return workflowTask;
        }
        return null;
    }

    @Override
    public Map<WorkflowTaskInputTypeEnum, WorkflowTask> setWorkflowTask(Long workflowId, Long workflowVersion, Optional<Integer> prePsiTaskStep, Integer taskStep, String senderIdentityId, Optional<Boolean> activationPrePsi, Optional<String> modelId, Optional<WorkflowTaskPowerTypeEnum> powerType, Optional<String> powerIdentityId) {
        Map<WorkflowTaskInputTypeEnum, WorkflowTask> result = new HashMap<>();
        // 主任务的设置
        WorkflowTask workflowTask = getByStep(workflowId, workflowVersion, taskStep);
        workflowTask.setIdentityId(senderIdentityId);
        activationPrePsi.ifPresent(item -> workflowTask.setInputPsi(item));
        modelId.ifPresent(item -> workflowTask.setInputModelId(item));
        powerType.ifPresent(item -> workflowTask.setPowerType(item));
        powerIdentityId.ifPresent(item -> workflowTask.setPowerIdentityId(item));
        updateById(workflowTask);
        result.put(WorkflowTaskInputTypeEnum.NORMAL, workflowTask);
        // 前置psi设置
        prePsiTaskStep.ifPresent(item -> {
            WorkflowTask psiWorkflowTask = getByStep(workflowId, workflowVersion, item);
            psiWorkflowTask.setIdentityId(senderIdentityId);
            activationPrePsi.ifPresent(item1 -> psiWorkflowTask.setEnable(item1));
            updateById(psiWorkflowTask);
            result.put(WorkflowTaskInputTypeEnum.PSI, psiWorkflowTask);
        });
        return result;
    }
}
