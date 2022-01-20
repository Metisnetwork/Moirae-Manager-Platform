package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.WorkflowNodeOutput;

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

    /**
     * 根据任务id获取输入放的组织id
     * @param taskId 任务id
     * @return identityId 组织id
     */
    String getOutputIdentityIdByTaskId(String taskId);

    /**
     * 批量保存节点输出
     *
     * @param workflowNodeOutputList 节点输出列表
     */
    void batchInsert(List<WorkflowNodeOutput> workflowNodeOutputList);

    List<WorkflowNodeOutput> queryByWorkflowNodeId(Long id);
}
