package com.moirae.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moirae.rosettaflow.common.enums.WorkflowCreateModeEnum;
import com.moirae.rosettaflow.manager.CalculationProcessManager;
import com.moirae.rosettaflow.manager.CalculationProcessStepManager;
import com.moirae.rosettaflow.manager.WorkflowManager;
import com.moirae.rosettaflow.manager.WorkflowVersionManager;
import com.moirae.rosettaflow.mapper.domain.CalculationProcess;
import com.moirae.rosettaflow.mapper.domain.Workflow;
import com.moirae.rosettaflow.mapper.domain.WorkflowVersion;
import com.moirae.rosettaflow.service.CommonService;
import com.moirae.rosettaflow.service.WorkflowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class WorkflowServiceImpl implements WorkflowService {

    @Resource
    private WorkflowManager workflowManager;
    @Resource
    private WorkflowVersionManager workflowVersionManager;
    @Resource
    private CalculationProcessManager calculationProcessManager;
    @Resource
    private CalculationProcessStepManager calculationProcessStepManager;
    @Resource
    private CommonService commonService;

    @Override
    public int getWorkflowCount() {
        return workflowManager.getWorkflowCount(commonService.getCurrentUser().getAddress());
    }

    @Override
    public IPage<Workflow> getWorkflowList(Long current, Long size, String keyword, Long algorithmId, Date begin, Date end) {
        Page<Workflow> page = new Page<>(current, size);
        return workflowManager.getWorkflowList(page, commonService.getCurrentUser().getAddress(), keyword, algorithmId, begin, end);
    }

    @Override
    public IPage<WorkflowVersion> getWorkflowVersionList(Long current, Long size, Long workflowId) {
        Page<WorkflowVersion> page = new Page<>(current, size);
        return workflowVersionManager.getWorkflowVersionList(page, workflowId);
    }

    @Override
    public List<CalculationProcess> getCalculationProcessList(Long algorithmId) {
        List<CalculationProcess> calculationProcessList = calculationProcessManager.getCalculationProcessList(algorithmId);
        calculationProcessList.stream().forEach(item -> {
            item.setStepItem(calculationProcessStepManager.getList(item.getCalculationProcessId()));
        });
        return calculationProcessList;
    }

    @Override
    public WorkflowVersion createWorkflowOfWizardMode(Workflow workflow) {
        return create(workflow, WorkflowCreateModeEnum.WIZARD_MODE);
    }

    private WorkflowVersion create(Workflow workflow, WorkflowCreateModeEnum createMode){
        // 创建工作流记录
        workflow.setCreateMode(createMode.ordinal());
        workflowManager.save(workflow);

        // 创建工作流版本
        WorkflowVersion workflowVersion = new WorkflowVersion();
        workflowVersion.setWorkflowId(workflow.getWorkflowId());
        workflowVersion.setWorkflowVersion(1L);
        workflowVersionManager.save(workflowVersion);

        // 向导模式创建任务
        if(createMode == WorkflowCreateModeEnum.WIZARD_MODE){
            createTaskOfWizard(workflow, workflowVersion);
        }
        return workflowVersion;
    }

    private void createTaskOfWizard(Workflow workflow, WorkflowVersion workflowVersion) {
        // 查询流程定义


        // 生成向导配置


        // 生成任务


    }
}
