package com.moirae.rosettaflow.manager;

import com.moirae.rosettaflow.mapper.domain.WorkflowTaskInput;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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
}
