package com.platon.rosettaflow.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.mapper.WorkflowNodeInputMapper;
import com.platon.rosettaflow.mapper.domain.WorkflowNodeInput;
import com.platon.rosettaflow.service.IWorkflowNodeInputService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/18
 * @description 工作流节点输入服务实现类
 */
@Slf4j
@Service
public class WorkflowNodeInputServiceImpl extends ServiceImpl<WorkflowNodeInputMapper, WorkflowNodeInput> implements IWorkflowNodeInputService {

    @Override
    public List<WorkflowNodeInput> getByWorkflowNodeId(Long workflowNodeId) {
        LambdaQueryWrapper<WorkflowNodeInput> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowNodeInput::getWorkflowNodeId, workflowNodeId);
        wrapper.orderByAsc(WorkflowNodeInput::getPartyId);
        return this.list(wrapper);
    }

    @Override
    public void deleteByWorkflowNodeId(Long workflowNodeId) {
        LambdaQueryWrapper<WorkflowNodeInput> delWrapper = Wrappers.lambdaQuery();
        delWrapper.eq(WorkflowNodeInput::getWorkflowNodeId, workflowNodeId);
        this.remove(delWrapper);
    }
}
