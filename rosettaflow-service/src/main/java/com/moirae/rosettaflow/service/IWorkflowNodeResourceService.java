package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.WorkflowNodeResource;

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
    WorkflowNodeResource queryByWorkflowNodeId(Long workflowNodeId);

    /**
     * 批量保存节点资源
     *
     * @param workflowNodeResourceList 节点资源列表
     */
    void batchInsert(List<WorkflowNodeResource> workflowNodeResourceList);
}
