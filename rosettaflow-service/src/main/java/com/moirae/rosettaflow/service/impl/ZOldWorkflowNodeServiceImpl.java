package com.moirae.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.mapper.ZOldWorkflowNodeMapper;
import com.moirae.rosettaflow.mapper.domain.ZOldWorkflow;
import com.moirae.rosettaflow.mapper.domain.ZOldWorkflowNode;
import com.moirae.rosettaflow.service.ZOldIWorkflowNodeService;
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
public class ZOldWorkflowNodeServiceImpl extends ServiceImpl<ZOldWorkflowNodeMapper, ZOldWorkflowNode> implements ZOldIWorkflowNodeService {

    @Override
    public void saveCopyWorkflowNodeTemp(ZOldWorkflow workflow, List<ZOldWorkflowNode> oldNodeList) {
        oldNodeList.forEach(oldNode -> this.copyWorkflowNode(oldNode, workflow));
    }

    /** 复制工作流节点 */
    private ZOldWorkflowNode copyWorkflowNode(ZOldWorkflowNode oldNode, ZOldWorkflow workflow) {
        ZOldWorkflowNode newNode = new ZOldWorkflowNode();
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
    public List<ZOldWorkflowNode> queryByWorkflowIdAndVersion(Long workflowId, Integer version) {
        LambdaQueryWrapper<ZOldWorkflowNode> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ZOldWorkflowNode::getWorkflowId, workflowId);
        wrapper.eq(ZOldWorkflowNode::getWorkflowEditVersion, version);
        return this.list(wrapper);
    }
}
