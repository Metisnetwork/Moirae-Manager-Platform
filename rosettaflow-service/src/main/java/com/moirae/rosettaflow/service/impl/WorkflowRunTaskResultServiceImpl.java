package com.moirae.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.mapper.WorkflowRunTaskResultMapper;
import com.moirae.rosettaflow.mapper.domain.WorkflowRunTaskResult;
import com.moirae.rosettaflow.service.IWorkflowRunTaskResultService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class WorkflowRunTaskResultServiceImpl extends ServiceImpl<WorkflowRunTaskResultMapper, WorkflowRunTaskResult> implements IWorkflowRunTaskResultService {
    @Override
    public List<WorkflowRunTaskResult> queryByTaskId(String taskId) {
        return getByTaskId(taskId);
    }

    private List<WorkflowRunTaskResult> getByTaskId(String taskId){
        LambdaQueryWrapper<WorkflowRunTaskResult> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowRunTaskResult::getTaskId, taskId);
        return this.list(wrapper);
    }
}
