package com.moirae.rosettaflow.manager;

import com.moirae.rosettaflow.common.enums.OldAndNewEnum;
import com.moirae.rosettaflow.mapper.domain.WorkflowTask;
import com.moirae.rosettaflow.mapper.domain.WorkflowTaskCode;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 工作流任务算法代码表 服务类
 * </p>
 *
 * @author chendai
 * @since 2022-03-30
 */
public interface WorkflowTaskCodeManager extends IService<WorkflowTaskCode> {

    Map<OldAndNewEnum, WorkflowTaskCode> copy(Long oldWorkflowTaskId, Long newWorkflowTaskId);

    WorkflowTaskCode deleteByWorkflowTaskId(Long workflowTaskId);
}
