package com.moirae.rosettaflow.manager.impl;

import com.moirae.rosettaflow.mapper.domain.WorkflowTaskOutput;
import com.moirae.rosettaflow.mapper.WorkflowTaskOutputMapper;
import com.moirae.rosettaflow.manager.WorkflowTaskOutputManager;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 项目工作流节点输出表 服务实现类
 * </p>
 *
 * @author chendai
 * @since 2022-03-30
 */
@Service
public class WorkflowTaskOutputManagerImpl extends ServiceImpl<WorkflowTaskOutputMapper, WorkflowTaskOutput> implements WorkflowTaskOutputManager {

}
