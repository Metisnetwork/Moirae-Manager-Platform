package com.moirae.rosettaflow.manager.impl;

import com.moirae.rosettaflow.mapper.domain.WorkflowRunTaskStatus;
import com.moirae.rosettaflow.mapper.WorkflowRunTaskStatusMapper;
import com.moirae.rosettaflow.manager.WorkflowRunTaskStatusManager;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 工作流任务运行状态 服务实现类
 * </p>
 *
 * @author chendai
 * @since 2022-03-30
 */
@Service
public class WorkflowRunTaskStatusManagerImpl extends ServiceImpl<WorkflowRunTaskStatusMapper, WorkflowRunTaskStatus> implements WorkflowRunTaskStatusManager {

}
