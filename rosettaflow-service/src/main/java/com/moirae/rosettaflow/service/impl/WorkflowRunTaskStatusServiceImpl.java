package com.moirae.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.mapper.WorkflowRunTaskStatusMapper;
import com.moirae.rosettaflow.mapper.domain.WorkflowRunTaskStatus;
import com.moirae.rosettaflow.service.IWorkflowRunTaskStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class WorkflowRunTaskStatusServiceImpl extends ServiceImpl<WorkflowRunTaskStatusMapper, WorkflowRunTaskStatus> implements IWorkflowRunTaskStatusService {
    @Override
    public List<WorkflowRunTaskStatus> listByWorkflowRunStatusId(Long workflowRunStatusId) {
        LambdaQueryWrapper<WorkflowRunTaskStatus> wrapper =  Wrappers.lambdaQuery();
        wrapper.eq(WorkflowRunTaskStatus::getWorkflowRunId, workflowRunStatusId);
        return this.list(wrapper);
    }

    @Override
    public List<WorkflowRunTaskStatus> queryUnConfirmedWorkflowRunTaskStatus() {
        return baseMapper.queryUnConfirmedWorkflowRunTaskStatus();
    }
}
