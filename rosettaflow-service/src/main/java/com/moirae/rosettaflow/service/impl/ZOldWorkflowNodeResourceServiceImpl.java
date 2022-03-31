package com.moirae.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.mapper.ZOldWorkflowNodeResourceMapper;
import com.moirae.rosettaflow.mapper.domain.ZOldWorkflowNodeResource;
import com.moirae.rosettaflow.service.ZOldIWorkflowNodeResourceService;
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
public class ZOldWorkflowNodeResourceServiceImpl extends ServiceImpl<ZOldWorkflowNodeResourceMapper, ZOldWorkflowNodeResource> implements ZOldIWorkflowNodeResourceService {
    @Override
    public ZOldWorkflowNodeResource queryByWorkflowNodeId(Long workflowNodeId) {
        LambdaQueryWrapper<ZOldWorkflowNodeResource> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ZOldWorkflowNodeResource::getWorkflowNodeId, workflowNodeId);
        return this.getOne(wrapper);
    }

    @Override
    public void batchInsert(List<ZOldWorkflowNodeResource> workflowNodeResourceList) {
        this.baseMapper.batchInsert(workflowNodeResourceList);
    }
}
