package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.dto.WorkflowNodeDto;
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
     * 清空工作流节点
     *
     * @param workflowId 工作流id
     */
    void clearWorkflowNode(Long workflowId);

    /**
     * 根据工作流id及节点序号获取工作流节点
     *
     * @param workflowId 工作流id
     * @param startNode  节点序号x
     * @return 工作流节点
     */
    WorkflowNode getByWorkflowIdAndStep(Long workflowId, Integer startNode);

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
     * @param newWorkflowId 新工作流id
     * @param oldNodeList   旧工作流列表
     */
    void saveCopyWorkflowNodeTemp(Long newWorkflowId, List<WorkflowNode> oldNodeList);

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
     * 获取所有运行中的节点
     *
     * @param beforeHour 开始前小时数
     * @return 运行中节点列表
     */
    List<WorkflowNode> getRunningNode(int beforeHour);

    /**
     * 更新工作流节点运行状态
     *
     * @param ids       id数组
     * @param runStatus 运行状态
     */
    void updateRunStatus(Object[] ids, Byte runStatus);

    List<WorkflowNode> queryByWorkflowIdAndVersion(Long workflowId, Integer version);
}
