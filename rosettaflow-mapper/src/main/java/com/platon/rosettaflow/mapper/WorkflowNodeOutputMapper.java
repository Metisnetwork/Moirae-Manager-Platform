package com.platon.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platon.rosettaflow.mapper.domain.WorkflowNodeOutput;

import java.util.List;

/**
 * @author admin
 */
public interface WorkflowNodeOutputMapper extends BaseMapper<WorkflowNodeOutput> {

    /**
     * 查询节点输出列表
     * @param workflowNodeId
     * @return
     */
    List<WorkflowNodeOutput> getByWorkflowNodeId(Long workflowNodeId);
}