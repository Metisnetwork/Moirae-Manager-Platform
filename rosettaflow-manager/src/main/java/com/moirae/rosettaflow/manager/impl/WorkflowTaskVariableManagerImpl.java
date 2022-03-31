package com.moirae.rosettaflow.manager.impl;

import com.moirae.rosettaflow.mapper.domain.WorkflowTaskVariable;
import com.moirae.rosettaflow.mapper.WorkflowTaskVariableMapper;
import com.moirae.rosettaflow.manager.WorkflowTaskVariableManager;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 工作流任务变量表 服务实现类
 * </p>
 *
 * @author chendai
 * @since 2022-03-30
 */
@Service
public class WorkflowTaskVariableManagerImpl extends ServiceImpl<WorkflowTaskVariableMapper, WorkflowTaskVariable> implements WorkflowTaskVariableManager {

}
