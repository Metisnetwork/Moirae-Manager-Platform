package com.platon.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platon.rosettaflow.mapper.domain.WorkflowNodeInput;

import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/18
 * @description 工作流节点输入服务接口
 */
public interface IWorkflowNodeInputService extends IService<WorkflowNodeInput> {
    /**
     * 根据工作流节点id获取工作流节点输入列表
     * @param workflowNodeId 工作流节点id
     * @return 工作流节点输入列表
     */
    List<WorkflowNodeInput> getByWorkflowNodeId(Long workflowNodeId);

    /**
     * 查询工作流节点输入相关数据
     * @param idList 表或字段id
     * @return WorkflowNodeInput
     */
    List<WorkflowNodeInput> queryWorkflowNodeRelatedData(List<Long> idList);

    /**
     * 物理删除工作流节点输入, 根据工作流节点id
     * @param workflowNodeId 工作流节点id
     */
    void deleteByWorkflowNodeId(Long workflowNodeId);

    /**
     * 逻辑删除工作流节点输入, 根据工作流节点id
     * @param workflowNodeId 工作流节点id
     */
    void deleteLogicByWorkflowNodeId(Long workflowNodeId);
}
