package com.moirae.rosettaflow.manager.impl;

import com.moirae.rosettaflow.mapper.domain.WorkflowRunStatus;
import com.moirae.rosettaflow.mapper.WorkflowRunStatusMapper;
import com.moirae.rosettaflow.manager.WorkflowRunStatusManager;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 工作流运行状态 服务实现类
 * </p>
 *
 * @author chendai
 * @since 2022-03-30
 */
@Service
public class WorkflowRunStatusManagerImpl extends ServiceImpl<WorkflowRunStatusMapper, WorkflowRunStatus> implements WorkflowRunStatusManager {

}
