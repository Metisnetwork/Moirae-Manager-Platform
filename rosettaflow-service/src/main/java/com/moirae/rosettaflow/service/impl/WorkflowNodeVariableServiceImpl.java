package com.moirae.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.mapper.WorkflowNodeVariableMapper;
import com.moirae.rosettaflow.mapper.domain.WorkflowNodeVariable;
import com.moirae.rosettaflow.service.IWorkflowNodeVariableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 工作流节点变量服务实现类
 * @author hudenian
 * @date 2021/8/18
 */
@Slf4j
@Service
public class WorkflowNodeVariableServiceImpl extends ServiceImpl<WorkflowNodeVariableMapper, WorkflowNodeVariable> implements IWorkflowNodeVariableService {
    @Override
    public List<WorkflowNodeVariable> queryByWorkflowNodeId(Long workflowNodeId) {
        LambdaQueryWrapper<WorkflowNodeVariable> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowNodeVariable::getWorkflowNodeId, workflowNodeId);
        return this.list(wrapper);
    }

    @Override
    public void batchInsert(List<WorkflowNodeVariable> workflowNodeVariableList) {
        this.baseMapper.batchInsert(workflowNodeVariableList);
    }
}
