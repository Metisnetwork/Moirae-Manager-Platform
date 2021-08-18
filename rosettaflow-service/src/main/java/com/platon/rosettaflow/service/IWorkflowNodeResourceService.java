package com.platon.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platon.rosettaflow.mapper.domain.WorkflowNodeResource;

/**
 * @author hudenian
 * @date 2021/8/18
 * @description 工作流节点资源接口
 */
public interface IWorkflowNodeResourceService extends IService<WorkflowNodeResource> {
    /**
     * 根据工作流节点id获取工作流节点资源
     *
     * @param workflowNodeId 工作流节点id
     * @return 工作流节点资源
     */
    WorkflowNodeResource getByWorkflowNodeId(Long workflowNodeId);
}
