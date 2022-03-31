package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.ZOldWorkflowRunTaskResult;

import java.util.List;

public interface ZOldIWorkflowRunTaskResultService extends IService<ZOldWorkflowRunTaskResult> {
    List<ZOldWorkflowRunTaskResult> queryByTaskId(String taskId);
}
