package com.moirae.rosettaflow.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.common.enums.OldAndNewEnum;
import com.moirae.rosettaflow.mapper.domain.AlgorithmVariable;
import com.moirae.rosettaflow.mapper.domain.WorkflowTaskVariable;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工作流任务变量表 服务类
 * </p>
 *
 * @author chendai
 * @since 2022-03-30
 */
public interface WorkflowTaskVariableManager extends IService<WorkflowTaskVariable> {

    List<WorkflowTaskVariable> listByWorkflowTaskId(Long workflowTaskId);

    boolean removeByWorkflowTaskIds(List<Long> workflowTaskIdList);

    List<Map<OldAndNewEnum, WorkflowTaskVariable>> copy(Long oldWorkflowTaskId, Long newWorkflowTaskId);

    List<WorkflowTaskVariable> deleteByWorkflowTaskId(Long workflowTaskId);

    boolean create(Long workflowTaskId, List<AlgorithmVariable> algorithmVariableList);
}
