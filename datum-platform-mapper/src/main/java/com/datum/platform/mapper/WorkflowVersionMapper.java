package com.datum.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.datum.platform.mapper.domain.WorkflowVersion;

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
