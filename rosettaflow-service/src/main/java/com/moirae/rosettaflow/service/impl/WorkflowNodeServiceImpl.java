package com.moirae.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.mapper.WorkflowNodeMapper;
import com.moirae.rosettaflow.mapper.domain.Workflow;
import com.moirae.rosettaflow.mapper.domain.WorkflowNode;
import com.moirae.rosettaflow.service.IWorkflowNodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 工作流节点服务实现类
 *
 * @author hudenian
 * @date 2021/8/31
 */
@Slf4j
@Service
public class WorkflowNodeServiceImpl extends ServiceImpl<WorkflowNodeMapper, WorkflowNode> implements IWorkflowNodeService {

    @Override
    public void saveCopyWorkflowNodeTemp(Workflow workflow, List<WorkflowNode> oldNodeList) {
        oldNodeList.forEach(oldNode -> this.copyWorkflowNode(oldNode, workflow));
    }

    /** 复制工作流节点 */
    private WorkflowNode copyWorkflowNode(WorkflowNode oldNode, Workflow workflow) {
        WorkflowNode newNode = new WorkflowNode();
        newNode.setWorkflowId(workflow.getId());
        newNode.setWorkflowEditVersion(workflow.getEditVersion());
        newNode.setAlgorithmId(oldNode.getAlgorithmId());
        newNode.setNodeName(oldNode.getNodeName());
        newNode.setNodeStep(oldNode.getNodeStep());
        newNode.setInputModel(oldNode.getInputModel());
        newNode.setModelId(oldNode.getModelId());
        this.save(newNode);
        return newNode;
    }

    @Override
    public List<WorkflowNode> queryByWorkflowIdAndVersion(Long workflowId, Integer version) {
        LambdaQueryWrapper<WorkflowNode> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowNode::getWorkflowId, workflowId);
        wrapper.eq(WorkflowNode::getWorkflowEditVersion, version);
        return this.list(wrapper);
    }
}
