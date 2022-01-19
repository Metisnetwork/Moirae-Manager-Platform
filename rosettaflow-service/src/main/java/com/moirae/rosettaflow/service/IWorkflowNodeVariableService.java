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
    List<WorkflowNodeVariable> queryByWorkflowNodeId(Long workflowNodeId);

    /**
     * 批量保存节点变量列表
     *
     * @param workflowNodeVariableList 节点变量列表
     */
    void batchInsert(List<WorkflowNodeVariable> workflowNodeVariableList);
}
