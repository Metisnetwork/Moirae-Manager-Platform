package com.moirae.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.mapper.ZOldWorkflowRunTaskStatusMapper;
import com.moirae.rosettaflow.mapper.domain.ZOldWorkflowRunTaskStatus;
import com.moirae.rosettaflow.service.ZOldIWorkflowRunTaskStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ZOldWorkflowRunTaskStatusServiceImpl extends ServiceImpl<ZOldWorkflowRunTaskStatusMapper, ZOldWorkflowRunTaskStatus> implements ZOldIWorkflowRunTaskStatusService {
    @Override
    public List<ZOldWorkflowRunTaskStatus> listByWorkflowRunStatusId(Long workflowRunStatusId) {
        LambdaQueryWrapper<ZOldWorkflowRunTaskStatus> wrapper =  Wrappers.lambdaQuery();
        wrapper.eq(ZOldWorkflowRunTaskStatus::getWorkflowRunId, workflowRunStatusId);
        return this.list(wrapper);
    }

    @Override
    public List<ZOldWorkflowRunTaskStatus> queryUnConfirmedWorkflowRunTaskStatus() {
        return baseMapper.queryUnConfirmedWorkflowRunTaskStatus();
    }
}
