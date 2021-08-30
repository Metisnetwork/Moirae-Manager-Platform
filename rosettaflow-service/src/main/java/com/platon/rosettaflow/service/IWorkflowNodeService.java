package com.platon.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platon.rosettaflow.mapper.domain.WorkflowNode;

import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/16
 * @description 工作流节点服务
 */
public interface IWorkflowNodeService extends IService<WorkflowNode> {
    /**
     * 根据工作流id及节点序号获取工作流节点
     *
     * @param workflowId 工作流id
     * @param startNode  节点序号
     * @return 工作流节点
     */
    WorkflowNode getByWorkflowIdAndStep(Long workflowId, Integer startNode);

    /**
     * 根据工作流id获取工作流节点列表
     * @param workflowId 工作流主键id
     * @return 工作流节点列表
     */
    List<WorkflowNode> getByWorkflowId(Long workflowId);
}
