package com.platon.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platon.rosettaflow.mapper.domain.WorkflowNode;
import com.platon.rosettaflow.mapper.domain.WorkflowNodeTemp;

import java.util.List;

/**
 * @author hudenian
 * @date 2021/9/7
 * @description 工作流节点模板表
 */
public interface IWorkflowNodeTempService extends IService<WorkflowNodeTemp> {
    /**
     * 清空工作流节点模板表
     */
    void truncate();

    /**
     * 根据工作流模板id获取工作流节点列表
     *
     * @param workflowTempId 工作流模板id
     */
    List<WorkflowNodeTemp> getByWorkflowTempId(Long workflowTempId);

    /**
     * 根据工作流模板id及工作流节点表列添加工作流节点模板列表
     *
     * @param workflowTemplateId 工作流节点模板id
     * @param workflowNodeList   工作流列表
     */
    void addWorkflowNodeList(long workflowTemplateId, List<WorkflowNode> workflowNodeList);
}
