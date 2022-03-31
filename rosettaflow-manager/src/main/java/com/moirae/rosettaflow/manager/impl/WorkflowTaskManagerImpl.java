package com.moirae.rosettaflow.manager.impl;

import com.moirae.rosettaflow.mapper.domain.WorkflowTask;
import com.moirae.rosettaflow.mapper.WorkflowTaskMapper;
import com.moirae.rosettaflow.manager.WorkflowTaskManager;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 工作流任务配置表 服务实现类
 * </p>
 *
 * @author chendai
 * @since 2022-03-30
 */
@Service
public class WorkflowTaskManagerImpl extends ServiceImpl<WorkflowTaskMapper, WorkflowTask> implements WorkflowTaskManager {

}
