package com.moirae.rosettaflow.manager;

import com.moirae.rosettaflow.mapper.domain.WorkflowTaskVariable;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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
}
