package com.moirae.rosettaflow.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.mapper.WorkflowRunTaskResultMapper;
import com.moirae.rosettaflow.mapper.domain.WorkflowRunTaskResult;
import com.moirae.rosettaflow.service.IWorkflowRunTaskResultService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WorkflowRunTaskResultServiceImpl extends ServiceImpl<WorkflowRunTaskResultMapper, WorkflowRunTaskResult> implements IWorkflowRunTaskResultService {
}
