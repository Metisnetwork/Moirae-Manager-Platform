package com.platon.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platon.rosettaflow.mapper.domain.WorkflowNodeResource;

import java.util.List;

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

    /**
     * 物理删除工作流节点资源表, 根据工作流节点id
     *
     * @param workflowNodeId 工作流节点id
     */
    void deleteByWorkflowNodeId(Long workflowNodeId);

    /**
     * 逻辑删除工作流节点资源表, 根据工作流节点id
     *
     * @param workflowNodeId 工作流节点id
     */
    void deleteLogicByWorkflowNodeId(Long workflowNodeId);

    /**
     * 复制工作流节点环境资源
     *
     * @param newNodeId 新节点id
     * @param oldNodeId 旧节点id
     * @return
     */
    WorkflowNodeResource copyWorkflowNodeResource(Long newNodeId, Long oldNodeId);

    /**
     * 批量保存节点资源
     *
     * @param workflowNodeResourceList 节点资源列表
     */
    void batchInsert(List<WorkflowNodeResource> workflowNodeResourceList);
}
