package com.platon.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platon.rosettaflow.dto.WorkflowNodeDto;
import com.platon.rosettaflow.mapper.domain.WorkflowNode;
import com.platon.rosettaflow.mapper.domain.WorkflowNodeTemp;

import java.util.List;

/**
 * 工作流节点服务
 *
 * @author hudenian
 * @date 2021/8/16
 */
public interface IWorkflowNodeService extends IService<WorkflowNode> {

    /**
     * 获取工作流节点详情列表
     *
     * @param id 工作流id
     * @return 工作流详情
     */
    List<WorkflowNodeDto> queryNodeDetailsList(Long id);

    /**
     * 保存工作流所有节点数据
     *
     * @param workflowId          工作流id
     * @param workflowNodeDtoList 工作流节点列表
     */
    void saveWorkflowAllNodeData(Long workflowId, List<WorkflowNodeDto> workflowNodeDtoList);

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
     * @param startNode  节点序号
     * @return 工作流节点
     */
    WorkflowNode getByWorkflowIdAndStep(Long workflowId, Integer startNode);

    /**
     * 根据工作流id获取所有工作流节点列表（查询所有数据）
     *
     * @param workflowId 工作流主键id
     * @return 工作流节点列表
     */
    List<WorkflowNode> getAllWorkflowNodeList(Long workflowId);

    /**
     * 根据工作流id获取工作流节点列表（只查询生效的数据）
     *
     * @param workflowId 工作流主键id
     * @return 工作流节点列表
     */
    List<WorkflowNode> getWorkflowNodeList(Long workflowId);

    /**
     * 复制保存工作流节点
     *
     * @param newWorkflowId 新工作流id
     * @param oldNodeList   旧工作流列表
     */
    void saveCopyWorkflowNode(Long newWorkflowId, List<WorkflowNode> oldNodeList);

    /**
     * 根据工作流节点模板添加工作流
     *
     * @param workflowId           工作流表id
     * @param workflowNodeTempList 工作流模板节点列表
     */
    void addWorkflowNodeByTemplate(Long workflowId, List<WorkflowNodeTemp> workflowNodeTempList);

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

    /**
     * 根据id查询工作流节点
     *
     * @param id 工作流主键id
     * @return 工作流节点
     */
    WorkflowNode getWorkflowNodeById(Long id);

    /**
     * 获取所有运行成功的节点taskId列表
     *
     * @param workflowId 工作流主键id
     * @return 运行中节点列表
     */
    List<String> getTaskIds(Long workflowId);
}
