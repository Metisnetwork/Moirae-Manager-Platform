package com.platon.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platon.rosettaflow.mapper.domain.WorkflowNodeOutput;

import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/18
 * @description 工作流节点输出接口
 */
public interface IWorkflowNodeOutputService extends IService<WorkflowNodeOutput> {
    /**
     * 根据工作流节点id获取工作流节点输出列表
     *
     * @param workflowNodeId 工作流节点id
     * @return 工作流节点输出列表
     */
    List<WorkflowNodeOutput> getByWorkflowNodeId(Long workflowNodeId);
}
