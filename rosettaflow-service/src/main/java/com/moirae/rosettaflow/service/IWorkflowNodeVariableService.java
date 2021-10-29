package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.AlgorithmVariable;
import com.moirae.rosettaflow.mapper.domain.WorkflowNodeVariable;

import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/18
 * @description 工作流节点变量服务接口
 */
public interface IWorkflowNodeVariableService extends IService<WorkflowNodeVariable> {
    /**
     * 根据工作流节点id获取工作流节点自变量及因变量列表
     *
     * @param workflowNodeId 工作流节点id
     * @return 工作流节点节点自变量及因变量列表
     */
    List<WorkflowNodeVariable> getByWorkflowNodeId(Long workflowNodeId);

    /**
     * 物物理批量删除工作流节点输入变量，根据节点id
     *
     * @param workflowNodeIdList 工作流节点id列表
     */
    void deleteByWorkflowNodeId(List<Long> workflowNodeIdList);

    /**
     * 根据算法变量添加工作流节点算法变量
     *
     * @param workflowNodeId        工作流节点id
     * @param algorithmVariableList 算法变量列表
     */
    void addByAlgorithmVariableList(Long workflowNodeId, List<AlgorithmVariable> algorithmVariableList);

    /**
     * 逻辑删除工作流节点变量， 根据工作流节点id
     *
     * @param workflowNodeId 工作流节点id
     */
    void deleteLogicByWorkflowNodeId(Long workflowNodeId);

    /**
     * 复制工作流节点变量
     *
     * @param newNodeId 新节点id
     * @param oldNodeId 旧节点id
     * @return 工作流节点变量列表
     */
    List<WorkflowNodeVariable> copyWorkflowNodeVariable(Long newNodeId, Long oldNodeId);

    /**
     * 批量保存节点变量列表
     *
     * @param workflowNodeVariableList 节点变量列表
     */
    void batchInsert(List<WorkflowNodeVariable> workflowNodeVariableList);
}
