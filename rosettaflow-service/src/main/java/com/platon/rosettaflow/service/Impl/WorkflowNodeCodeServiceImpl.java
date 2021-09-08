package com.platon.rosettaflow.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.mapper.WorkflowNodeCodeMapper;
import com.platon.rosettaflow.mapper.domain.WorkflowNodeCode;
import com.platon.rosettaflow.service.IWorkflowNodeCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 工作流代码服务实现类
 * @author hudenian
 * @date 2021/8/18
 */
@Slf4j
@Service
public class WorkflowNodeCodeServiceImpl extends ServiceImpl<WorkflowNodeCodeMapper, WorkflowNodeCode> implements IWorkflowNodeCodeService {
    @Override
    public WorkflowNodeCode getByWorkflowNodeId(Long workflowNodeId) {
        LambdaQueryWrapper<WorkflowNodeCode> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowNodeCode::getWorkflowNodeId, workflowNodeId);
        wrapper.eq(WorkflowNodeCode::getStatus, 1);
        return this.getOne(wrapper);
    }

    @Override
    public void deleteByWorkflowNodeId(Long workflowNodeId) {
        LambdaQueryWrapper<WorkflowNodeCode> delWrapper = Wrappers.lambdaQuery();
        delWrapper.eq(WorkflowNodeCode::getWorkflowNodeId, workflowNodeId);
        this.remove(delWrapper);
    }

    @Override
    public void deleteLogicByWorkflowNodeId(Long workflowNodeId) {
        LambdaUpdateWrapper<WorkflowNodeCode> delWrapper = Wrappers.lambdaUpdate();
        delWrapper.eq(WorkflowNodeCode::getWorkflowNodeId, workflowNodeId);
        delWrapper.set(WorkflowNodeCode::getStatus, 0);
        this.update(delWrapper);
    }
}
