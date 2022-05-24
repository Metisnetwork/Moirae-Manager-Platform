package com.moirae.rosettaflow.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.Workflow;
import com.moirae.rosettaflow.mapper.domain.WorkflowVersion;

import java.util.List;

/**
 * <p>
 * 工作流不同版本设置表 服务类
 * </p>
 *
 * @author chendai
 * @since 2022-03-30
 */
public interface WorkflowVersionManager extends IService<WorkflowVersion> {

    IPage<WorkflowVersion> getWorkflowVersionList(Page<WorkflowVersion> page, Long workflowId);

    WorkflowVersion create(Long workflowId, Long workflowVersion, String workflowVersionName);

    List<WorkflowVersion> deleteByWorkflowId(Long workflowId);

    WorkflowVersion getById(Long workflowId, Long workflowVersion);

    List<WorkflowVersion> listByNameAndId(Long workflowId, String workflowVersionName);
}
