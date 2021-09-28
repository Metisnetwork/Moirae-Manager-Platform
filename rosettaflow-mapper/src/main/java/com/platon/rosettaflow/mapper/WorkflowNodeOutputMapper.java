package com.platon.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platon.rosettaflow.mapper.domain.WorkflowNodeOutput;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author admin
 */
public interface WorkflowNodeOutputMapper extends BaseMapper<WorkflowNodeOutput> {

    /**
     * 查询节点输出列表
     *
     * @param workflowNodeId 工作流节点id
     * @return 节点输出列表
     */
    List<WorkflowNodeOutput> getByWorkflowNodeId(Long workflowNodeId);

    /**
     * 批量保存节点输出
     *
     * @param workflowNodeOutputList 节点输出列表
     * @return 保存记录数
     */
    int batchInsert(@Param("workflowNodeOutputList") List<WorkflowNodeOutput> workflowNodeOutputList);
}