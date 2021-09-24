package com.platon.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.common.enums.StatusEnum;
import com.platon.rosettaflow.mapper.WorkflowNodeResourceMapper;
import com.platon.rosettaflow.mapper.domain.WorkflowNodeResource;
import com.platon.rosettaflow.service.IWorkflowNodeResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    public void deleteByWorkflowNodeId(Long workflowNodeId) {
        LambdaQueryWrapper<WorkflowNodeResource> delWrapper = Wrappers.lambdaQuery();
        delWrapper.eq(WorkflowNodeResource::getWorkflowNodeId, workflowNodeId);
        this.remove(delWrapper);
    }

    @Override
    public void deleteLogicByWorkflowNodeId(Long workflowNodeId) {
        LambdaUpdateWrapper<WorkflowNodeResource> delWrapper = Wrappers.lambdaUpdate();
        delWrapper.eq(WorkflowNodeResource::getWorkflowNodeId, workflowNodeId);
        delWrapper.set(WorkflowNodeResource::getStatus, StatusEnum.UN_VALID.getValue());
        this.update(delWrapper);
    }
}
