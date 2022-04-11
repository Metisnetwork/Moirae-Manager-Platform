package com.moirae.rosettaflow.manager;

import com.moirae.rosettaflow.mapper.domain.WorkflowTaskOutput;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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
}
