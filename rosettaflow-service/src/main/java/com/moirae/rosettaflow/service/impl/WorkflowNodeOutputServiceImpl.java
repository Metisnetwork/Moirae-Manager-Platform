package com.moirae.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.mapper.WorkflowNodeOutputMapper;
import com.moirae.rosettaflow.mapper.domain.WorkflowNodeOutput;
import com.moirae.rosettaflow.service.IWorkflowNodeOutputService;
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
        return baseMapper.getWorkflowNodeOutputAndOrgNameByNodeId(workflowNodeId);
    }

    @Override
    public void deleteByWorkflowNodeId(List<Long> workflowNodeIdList) {
        LambdaQueryWrapper<WorkflowNodeOutput> delWrapper = Wrappers.lambdaQuery();
        delWrapper.in(WorkflowNodeOutput::getWorkflowNodeId, workflowNodeIdList);
        this.remove(delWrapper);
    }

    @Override
    public List<WorkflowNodeOutput> copyWorkflowNodeOutput(Long newNodeId, Long oldNodeId) {
        List<WorkflowNodeOutput> oldNodeOutputList = this.getByWorkflowNodeId(oldNodeId);
        if (null == oldNodeOutputList || oldNodeOutputList.size() == 0) {
            return new ArrayList<>();
        }
        List<WorkflowNodeOutput> newNodeOutputList = new ArrayList<>();
        oldNodeOutputList.forEach(oldNodeOutput -> {
            WorkflowNodeOutput newNodeOutput = new WorkflowNodeOutput();
            newNodeOutput.setWorkflowNodeId(newNodeId);
            newNodeOutput.setIdentityId(oldNodeOutput.getIdentityId());
            //todo
//            newNodeOutput.setSenderFlag(oldNodeOutput.getSenderFlag());
            newNodeOutputList.add(newNodeOutput);
        });
        return newNodeOutputList;
    }

    @Override
    public String getOutputIdentityIdByTaskId(String taskId){
        return this.baseMapper.getOutputIdentityIdByTaskId(taskId);
    }

    @Override
    public String getOutputIdentityIdByWorkFlowIdAndStep(Long workflowNodeId, Long nodeStep){
        return this.baseMapper.getOutputIdentityIdByWorkFlowIdAndStep(workflowNodeId,nodeStep);
    }


    @Override
    public void batchInsert(List<WorkflowNodeOutput> workflowNodeOutputList) {
        this.baseMapper.batchInsert(workflowNodeOutputList);
    }
}
