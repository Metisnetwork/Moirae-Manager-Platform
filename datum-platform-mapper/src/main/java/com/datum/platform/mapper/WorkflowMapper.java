package com.datum.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.datum.platform.mapper.domain.Workflow;

import java.util.Date;

/**
 * <p>
 * 工作流表 Mapper 接口
 * </p>
 *
 * @author chendai
 * @since 2022-03-30
 */
public interface WorkflowMapper extends BaseMapper<Workflow> {

    IPage<Workflow> getWorkflowList(Page<Workflow> page, String address, String keyword, Long algorithmId, Date begin, Date end, Integer createMode);

    void updateLastRunTime(Long workflowId);
}
