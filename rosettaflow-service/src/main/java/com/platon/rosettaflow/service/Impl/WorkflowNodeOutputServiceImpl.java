package com.platon.rosettaflow.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.mapper.WorkflowNodeOutputMapper;
import com.platon.rosettaflow.mapper.domain.WorkflowNodeOutput;
import com.platon.rosettaflow.service.IWorkflowNodeOutputService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
        LambdaQueryWrapper<WorkflowNodeOutput> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowNodeOutput::getWorkflowNodeId, workflowNodeId);
        wrapper.orderByAsc(WorkflowNodeOutput::getPartyId);
        return this.list(wrapper);
    }
}
