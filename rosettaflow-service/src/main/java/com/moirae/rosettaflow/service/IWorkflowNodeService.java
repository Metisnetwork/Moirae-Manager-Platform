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
     * 模板复制保存工作流节点
     *
     * @param workflow 新工作流id
     * @param oldNodeList   旧工作流列表
     */
    void saveCopyWorkflowNodeTemp(Workflow workflow, List<WorkflowNode> oldNodeList);

    List<WorkflowNode> queryByWorkflowIdAndVersion(Long workflowId, Integer version);
}
