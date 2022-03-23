package com.moirae.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.mapper.WorkflowNodeResourceMapper;
import com.moirae.rosettaflow.mapper.domain.WorkflowNodeResource;
import com.moirae.rosettaflow.service.IWorkflowNodeResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/18
 * @description 工作流节点资源服务实现类
 */
@Slf4j
@Service
public class WorkflowNodeResourceServiceImpl extends ServiceImpl<WorkflowNodeResourceMapper, WorkflowNodeResource> implements IWorkflowNodeResourceService {
    @Override
    public WorkflowNodeResource queryByWorkflowNodeId(Long workflowNodeId) {
        LambdaQueryWrapper<WorkflowNodeResource> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowNodeResource::getWorkflowNodeId, workflowNodeId);
        return this.getOne(wrapper);
    }

    @Override
    public void batchInsert(List<WorkflowNodeResource> workflowNodeResourceList) {
        this.baseMapper.batchInsert(workflowNodeResourceList);
    }
}
