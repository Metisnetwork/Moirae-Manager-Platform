package com.moirae.rosettaflow.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.common.enums.OldAndNewEnum;
import com.moirae.rosettaflow.mapper.domain.WorkflowTaskInput;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工作流任务输入表 服务类
 * </p>
 *
 * @author chendai
 * @since 2022-03-30
 */
public interface WorkflowTaskInputManager extends IService<WorkflowTaskInput> {

    List<WorkflowTaskInput> listByWorkflowTaskId(Long workflowTaskId);

    void clearAndSave(Long workflowTaskId, List<WorkflowTaskInput> trainingWorkflowTaskInputList);

    boolean removeByWorkflowTaskIds(List<Long> workflowTaskIdList);

    List<Map<OldAndNewEnum, WorkflowTaskInput>> copy(Long oldWorkflowTaskId, Long newWorkflowTaskId);

    List<WorkflowTaskInput> deleteByWorkflowTaskId(Long workflowTaskId);

    boolean setWorkflowTaskInput(Long psiWorkflowTaskId, Long workflowTaskId, List<WorkflowTaskInput> workflowTaskInputList);
}
