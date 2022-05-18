package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.manager.WorkflowManager;
import com.moirae.rosettaflow.mapper.WorkflowMapper;
import com.moirae.rosettaflow.mapper.domain.Workflow;
import com.moirae.rosettaflow.mapper.enums.WorkflowCreateModeEnum;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 工作流表 服务实现类
 * </p>
 *
 * @author chendai
 * @since 2022-03-30
 */
@Service
public class WorkflowManagerImpl extends ServiceImpl<WorkflowMapper, Workflow> implements WorkflowManager {

    @Override
    public int getWorkflowCount(String address) {
        LambdaQueryWrapper<Workflow> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Workflow::getAddress, address);
        return count(wrapper);
    }

    @Override
    public IPage<Workflow> getWorkflowList(Page<Workflow> page, String address, String keyword, Long algorithmId, Date begin, Date end, Integer createMode) {
        return baseMapper.getWorkflowList(page, address, keyword, algorithmId, begin, end, createMode);
    }

    @Override
    public boolean updateStep(Long workflowId, Integer step, Boolean isSettingCompleted) {
        LambdaUpdateWrapper<Workflow> wrapper = Wrappers.lambdaUpdate();
        wrapper.set(Workflow::getCalculationProcessStep, step);
        wrapper.set(Workflow::getIsSettingCompleted, isSettingCompleted);
        wrapper.eq(Workflow::getWorkflowId, workflowId);
        return update(wrapper);
    }

    @Override
    public Workflow increaseVersion(Long workflowId) {
        Workflow workflow = getById(workflowId);
        workflow.setWorkflowVersion(workflow.getWorkflowVersion() + 1);
        updateById(workflow);
        return workflow;
    }

    @Override
    public Workflow delete(Long workflowId) {
        Workflow workflow = getById(workflowId);
        removeById(workflow.getWorkflowId());
        return workflow;
    }

    @Override
    public Workflow createOfWizardMode(String workflowName, String workflowDesc, Long algorithmId, String algorithmName, Long calculationProcessId, String calculationProcessName, String address) {
        Workflow workflow = new Workflow();
        workflow.setCreateMode(WorkflowCreateModeEnum.WIZARD_MODE);
        workflow.setWorkflowName(workflowName);
        workflow.setWorkflowDesc(workflowDesc);
        workflow.setAlgorithmId(algorithmId);
        workflow.setAlgorithmName(algorithmName);
        workflow.setCalculationProcessId(calculationProcessId);
        workflow.setCalculationProcessName(calculationProcessName);
        workflow.setCalculationProcessStep(0);
        workflow.setWorkflowVersion(1L);
        workflow.setAddress(address);
        if(save(workflow)){
            return workflow;
        }else {
            return null;
        }
    }

    @Override
    public Workflow createOfExpertMode(String workflowName, String address) {
        Workflow workflow = new Workflow();
        workflow.setCreateMode(WorkflowCreateModeEnum.EXPERT_MODE);
        workflow.setWorkflowName(workflowName);
        workflow.setWorkflowVersion(1L);
        workflow.setAddress(address);
        if(save(workflow)){
            return workflow;
        }else {
            return null;
        }
    }

    @Override
    public void updateLastRunTime(Long workflowId) {
        this.baseMapper.updateLastRunTime(workflowId);
    }
}
