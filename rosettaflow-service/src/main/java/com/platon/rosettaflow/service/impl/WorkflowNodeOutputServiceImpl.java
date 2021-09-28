package com.platon.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.mapper.WorkflowNodeOutputMapper;
import com.platon.rosettaflow.mapper.domain.WorkflowNodeOutput;
import com.platon.rosettaflow.service.IWorkflowNodeOutputService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/18
 * @description 作流节点输出服务实现类
 */
@Slf4j
@Service
public class WorkflowNodeOutputServiceImpl extends ServiceImpl<WorkflowNodeOutputMapper, WorkflowNodeOutput> implements IWorkflowNodeOutputService {
    @Override
    public List<WorkflowNodeOutput> getByWorkflowNodeId(Long workflowNodeId) {
        return baseMapper.getByWorkflowNodeId(workflowNodeId);
    }

    @Override
    public void deleteByWorkflowNodeId(Long workflowNodeId) {
        LambdaQueryWrapper<WorkflowNodeOutput> delWrapper = Wrappers.lambdaQuery();
        delWrapper.eq(WorkflowNodeOutput::getWorkflowNodeId, workflowNodeId);
        this.remove(delWrapper);
    }

    @Override
    public List<WorkflowNodeOutput> copyWorkflowNodeOutput(Long newNodeId, Long oldNodeId) {
        List<WorkflowNodeOutput> oldNodeOutputList = this.getByWorkflowNodeId(oldNodeId);
        List<WorkflowNodeOutput> newNodeOutputList = new ArrayList<>();
        if (oldNodeOutputList.size() > 0) {
            oldNodeOutputList.forEach(oldNodeOutput -> {
                WorkflowNodeOutput newNodeOutput = new WorkflowNodeOutput();
                newNodeOutput.setWorkflowNodeId(newNodeId);
                newNodeOutput.setIdentityId(oldNodeOutput.getIdentityId());
                newNodeOutput.setSenderFlag(oldNodeOutput.getSenderFlag());
                newNodeOutputList.add(newNodeOutput);
            });
        }
        return newNodeOutputList;
    }

    @Override
    public void batchInsert(List<WorkflowNodeOutput> workflowNodeOutputList) {
        this.baseMapper.batchInsert(workflowNodeOutputList);
    }
}
