package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.Workflow;
import com.moirae.rosettaflow.mapper.domain.WorkflowNode;

import java.util.List;

/**
 * 工作流节点服务
 *
 * @author hudenian
 * @date 2021/8/16
 */
public interface IWorkflowNodeService extends IService<WorkflowNode> {

    /**
     * 根据工作流id获取工作流节点列表（只查询生效的数据）
     *
     * @param workflowId 工作流主键id
     * @return 工作流节点列表
     */
    List<WorkflowNode> getWorkflowNodeList(Long workflowId);

    /**
     * 模板复制保存工作流节点
     *
     * @param workflow 新工作流id
     * @param oldNodeList   旧工作流列表
     */
    void saveCopyWorkflowNodeTemp(Workflow workflow, List<WorkflowNode> oldNodeList);

    /**
     * 获取正在运行的节点
     *
     * @param workflowId 工作流id
     * @return 正在运行的节点
     */
    WorkflowNode getRunningNodeByWorkflowId(Long workflowId);

    /**
     * 更新工作流节点运行状态
     *
     * @param workflowId   工作流id
     * @param oldRunStatus 旧的运行状态
     * @param newRunStatus 新的运行状态
     */
    void updateRunStatusByWorkflowId(Long workflowId, Byte oldRunStatus, Byte newRunStatus);

    /**
     * 更新工作流节点运行状态
     *
     * @param ids       id数组
     * @param runStatus 运行状态
     */
    void updateRunStatus(Object[] ids, Byte runStatus);

    List<WorkflowNode> queryByWorkflowIdAndVersion(Long workflowId, Integer version);
}
