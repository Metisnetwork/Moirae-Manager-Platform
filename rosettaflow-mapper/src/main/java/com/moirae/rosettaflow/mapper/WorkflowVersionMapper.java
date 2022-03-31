package com.moirae.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moirae.rosettaflow.mapper.domain.WorkflowVersion;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 工作流不同版本设置表 Mapper 接口
 * </p>
 *
 * @author chendai
 * @since 2022-03-30
 */
public interface WorkflowVersionMapper extends BaseMapper<WorkflowVersion> {

    IPage<WorkflowVersion> getWorkflowVersionList(Page<WorkflowVersion> page, Long workflowId);
}
