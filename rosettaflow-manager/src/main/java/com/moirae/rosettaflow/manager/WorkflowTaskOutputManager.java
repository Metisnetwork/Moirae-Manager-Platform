package com.moirae.rosettaflow.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.common.enums.OldAndNewEnum;
import com.moirae.rosettaflow.mapper.domain.WorkflowTaskOutput;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 项目工作流节点输出表 服务类
 * </p>
 *
 * @author chendai
 * @since 2022-03-30
 */
public interface WorkflowTaskOutputManager extends IService<WorkflowTaskOutput> {

    List<WorkflowTaskOutput> listByWorkflowTaskId(Long workflowTaskId);

    void clearAndSave(Long workflowTaskId, List<WorkflowTaskOutput> workflowTaskOutputList);

    boolean removeByWorkflowTaskIds(List<Long> workflowTaskIdList);

    List<Map<OldAndNewEnum, WorkflowTaskOutput>> copy(Long oldWorkflowTaskId, Long newWorkflowTaskId);

    List<WorkflowTaskOutput> deleteByWorkflowTaskId(Long workflowTaskId);
}
