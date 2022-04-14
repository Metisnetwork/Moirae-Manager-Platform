package com.moirae.rosettaflow.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.common.enums.OldAndNewEnum;
import com.moirae.rosettaflow.mapper.domain.WorkflowSettingExpert;
import com.moirae.rosettaflow.mapper.domain.WorkflowSettingWizard;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工作流向导模式步骤表 服务类
 * </p>
 *
 * @author chendai
 * @since 2022-03-30
 */
public interface WorkflowSettingWizardManager extends IService<WorkflowSettingWizard> {

    WorkflowSettingWizard getOneByStep(Long workflowId, Long workflowVersion, Integer step);

    List<Map<OldAndNewEnum, WorkflowSettingWizard>> copyAndReset(Long workflowId, Long oldVersion, Long newVersion);

    List<WorkflowSettingWizard> deleteWorkflowId(Long workflowId);
}
