package com.platon.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.common.enums.StatusEnum;
import com.platon.rosettaflow.mapper.WorkflowNodeResourceMapper;
import com.platon.rosettaflow.mapper.domain.WorkflowNodeCode;
import com.platon.rosettaflow.mapper.domain.WorkflowNodeResource;
import com.platon.rosettaflow.service.IWorkflowNodeResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author hudenian
 * @date 2021/8/18
 * @description 工作流节点资源服务实现类
 */
@Slf4j
@Service
public class WorkflowNodeResourceServiceImpl extends ServiceImpl<WorkflowNodeResourceMapper, WorkflowNodeResource> implements IWorkflowNodeResourceService {
    @Override
    public WorkflowNodeResource getByWorkflowNodeId(Long workflowNodeId) {
        LambdaQueryWrapper<WorkflowNodeResource> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowNodeResource::getWorkflowNodeId, workflowNodeId);
        wrapper.eq(WorkflowNodeResource::getStatus, StatusEnum.VALID.getValue());
        return this.getOne(wrapper);
    }

    @Override
    public void deleteByWorkflowNodeId(List<Long> workflowNodeIdList) {
        LambdaQueryWrapper<WorkflowNodeResource> delWrapper = Wrappers.lambdaQuery();
        delWrapper.in(WorkflowNodeResource::getWorkflowNodeId, workflowNodeIdList);
        this.remove(delWrapper);
    }

    @Override
    public void deleteLogicByWorkflowNodeId(Long workflowNodeId) {
        LambdaUpdateWrapper<WorkflowNodeResource> delWrapper = Wrappers.lambdaUpdate();
        delWrapper.eq(WorkflowNodeResource::getWorkflowNodeId, workflowNodeId);
        delWrapper.set(WorkflowNodeResource::getStatus, StatusEnum.UN_VALID.getValue());
        this.update(delWrapper);
    }

    @Override
    public WorkflowNodeResource copyWorkflowNodeResource(Long newNodeId, Long oldNodeId) {
        WorkflowNodeResource oldNodeResource = this.getByWorkflowNodeId(oldNodeId);
        if (Objects.isNull(oldNodeResource)) {
            return null;
        }
        WorkflowNodeResource newNodeResource = new WorkflowNodeResource();
        newNodeResource.setWorkflowNodeId(newNodeId);
        newNodeResource.setCostMem(oldNodeResource.getCostMem());
        newNodeResource.setCostCpu(oldNodeResource.getCostCpu());
        newNodeResource.setCostGpu(oldNodeResource.getCostGpu());
        newNodeResource.setCostBandwidth(oldNodeResource.getCostBandwidth());
        return newNodeResource;
    }

    @Override
    public void batchInsert(List<WorkflowNodeResource> workflowNodeResourceList) {
        this.baseMapper.batchInsert(workflowNodeResourceList);
    }
}
