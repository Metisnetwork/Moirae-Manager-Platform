package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moirae.rosettaflow.mapper.domain.WorkflowVersion;
import com.moirae.rosettaflow.mapper.WorkflowVersionMapper;
import com.moirae.rosettaflow.manager.WorkflowVersionManager;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 工作流不同版本设置表 服务实现类
 * </p>
 *
 * @author chendai
 * @since 2022-03-30
 */
@Service
public class WorkflowVersionManagerImpl extends ServiceImpl<WorkflowVersionMapper, WorkflowVersion> implements WorkflowVersionManager {

    @Override
    public IPage<WorkflowVersion> getWorkflowVersionList(Page<WorkflowVersion> page, Long workflowId) {
        return baseMapper.getWorkflowVersionList(page, workflowId);
    }
}
