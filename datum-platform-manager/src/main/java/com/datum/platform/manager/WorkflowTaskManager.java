package com.datum.platform.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.datum.platform.common.enums.OldAndNewEnum;
import com.datum.platform.common.enums.WorkflowTaskInputTypeEnum;
import com.datum.platform.mapper.domain.WorkflowTask;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * 工作流任务配置表 服务类
 * </p>
 *
 * @author chendai
 * @since 2022-03-30
 */
public interface WorkflowTaskManager extends IService<WorkflowTask> {

    WorkflowTask getByStep(Long workflowId, Long workflowVersion, Integer taskStep);

    List<WorkflowTask> listByWorkflowVersion(Long workflowId, Long workflowVersion);

    List<WorkflowTask> listByWorkflowVersionAndSteps(Long workflowId, Long workflowVersion, List<Integer> collect);

    List<Map<OldAndNewEnum, WorkflowTask>> copy(Long workflowId, Long oldVersion, Long newVersion);

    List<WorkflowTask> deleteWorkflowId(Long workflowId);

    List<WorkflowTask> listExecutableByWorkflowVersion(Long workflowId, Long workflowVersion);

    WorkflowTask createOfWizardMode(Long workflowId, Long workflowVersion, Integer step, Long algorithmId, Boolean inputModel, Integer inputModelStep, Boolean inputPsi, Integer inputPsiStep);

    Map<WorkflowTaskInputTypeEnum, WorkflowTask> setWorkflowTask(Long workflowId, Long workflowVersion, Integer psiTaskStep, Integer taskStep, String identityId, Boolean inputPsi, Optional<String> modelId);
}
