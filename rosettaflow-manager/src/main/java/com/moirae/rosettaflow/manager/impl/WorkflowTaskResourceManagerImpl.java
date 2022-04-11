package com.moirae.rosettaflow.manager.impl;

import com.moirae.rosettaflow.mapper.domain.WorkflowTaskResource;
import com.moirae.rosettaflow.mapper.WorkflowTaskResourceMapper;
import com.moirae.rosettaflow.manager.WorkflowTaskResourceManager;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 工作流任务资源表 服务实现类
 * </p>
 *
 * @author chendai
 * @since 2022-03-30
 */
@Service
public class WorkflowTaskResourceManagerImpl extends ServiceImpl<WorkflowTaskResourceMapper, WorkflowTaskResource> implements WorkflowTaskResourceManager {

    @Override
    public void clearAndSave(Long workflowTaskId, WorkflowTaskResource workflowTaskResource) {
        removeById(workflowTaskId);
        save(workflowTaskResource);
    }
}
