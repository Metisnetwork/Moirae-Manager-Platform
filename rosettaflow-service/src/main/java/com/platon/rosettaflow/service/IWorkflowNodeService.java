package com.platon.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platon.rosettaflow.mapper.domain.*;

import java.util.List;

/**
 * 工作流节点服务
 *
 * @author hudenian
 * @date 2021/8/16
 */
public interface IWorkflowNodeService extends IService<WorkflowNode> {

    /**
     * 保存工作流节点
     *
     * @param workflowId       工作流id
     * @param workflowNodeList 工作流节点信息列表
     */
    void saveWorkflowNode(Long workflowId, List<WorkflowNode> workflowNodeList);

    /**
     * 清空工作流节点
     *
     * @param workflowId 工作流id
     */
    void clearWorkflowNode(Long workflowId);

    /**
     * 添加工作流节点
     *
     * @param workflowNode 节点信息
     * @return Long
     */
    Long addWorkflowNode(WorkflowNode workflowNode);

    /**
     * 工作流节点重命名
     *
     * @param workflowNodeId 工作流节点id
     * @param nodeName       工作流节点名称
     */
    void renameWorkflowNode(Long workflowNodeId, String nodeName);

    /**
     * 删除工作流中的节点
     *
     * @param id 工作流节点id
     */
    void deleteWorkflowNode(Long id);

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
     * 根据id查询工作流节点
     *
     * @param id 工作流主键id
     * @return 工作流节点列表
     */
    WorkflowNode getWorkflowNodeById(Long id);

    /**
     * 添加工作流节点输入数据
     *
     * @param workflowNodeId        工作流节点
     * @param workflowNodeInputList 节点输入list
     */
    void saveWorkflowNodeInput(Long workflowNodeId, List<WorkflowNodeInput> workflowNodeInputList);

    /**
     * 添加工作流节点输出数据
     *
     * @param workflowNodeId
     * @param workflowNodeOutputList
     */
    void saveWorkflowNodeOutput(Long workflowNodeId, List<WorkflowNodeOutput> workflowNodeOutputList);

    /**
     * 添加工作流节点代码
     *
     * @param workflowNodeCode 工作流节点代码
     */
    void saveWorkflowNodeCode(WorkflowNodeCode workflowNodeCode);

    /**
     * 添加工作流节点资源
     *
     * @param workflowNodeResource 工作流节点资源
     */
    void saveWorkflowNodeResource(WorkflowNodeResource workflowNodeResource);

    /**
     * 复制保存工作流节点
     *
     * @param newWorkflowId
     * @param workflowNodeOldList
     */
    void copySaveWorkflowNode(Long newWorkflowId, List<WorkflowNode> workflowNodeOldList);

    /**
     * 根据工作流节点模板添加工作流
     *
     * @param workflowId           工作流表id
     * @param workflowNodeTempList 工作流模板节点列表
     */
    void addWorkflowNodeByTemplate(Long workflowId, List<WorkflowNodeTemp> workflowNodeTempList);
}
