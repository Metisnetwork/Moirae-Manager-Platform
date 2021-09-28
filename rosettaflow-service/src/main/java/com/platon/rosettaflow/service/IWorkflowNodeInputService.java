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
     *
     * @param workflowNodeId 工作流节点id
     * @return 工作流节点输入列表
     */
    List<WorkflowNodeInput> getByWorkflowNodeId(Long workflowNodeId);

    /**
     * 物理删除工作流节点输入, 根据工作流节点id
     *
     * @param workflowNodeId 工作流节点id
     */
    void deleteByWorkflowNodeId(Long workflowNodeId);

    /**
     * 逻辑删除工作流节点输入, 根据工作流节点id
     *
     * @param workflowNodeId 工作流节点id
     */
    void deleteLogicByWorkflowNodeId(Long workflowNodeId);

    /**
     * 复制工作流节点输入数据
     *
     * @param newNodeId 新工作流节点id
     * @param oldNodeId 旧工作流节点id
     * @return WorkflowNodeInput
     */
    List<WorkflowNodeInput> copyWorkflowNodeInput(Long newNodeId, Long oldNodeId);

    /**
     * 批量保存节点输入
     *
     * @param workflowNodeInputList 节点输入列表
     */
    void batchInsert(List<WorkflowNodeInput> workflowNodeInputList);
}
