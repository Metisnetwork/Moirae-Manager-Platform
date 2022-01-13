package com.moirae.rosettaflow.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.mapper.WorkflowRunTaskStatusMapper;
import com.moirae.rosettaflow.mapper.domain.WorkflowRunTaskStatus;
import com.moirae.rosettaflow.service.IWorkflowRunTaskStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class IWorkflowRunTaskStatusServiceImpl extends ServiceImpl<WorkflowRunTaskStatusMapper, WorkflowRunTaskStatus> implements IWorkflowRunTaskStatusService {
}
