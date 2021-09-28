package com.platon.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platon.rosettaflow.mapper.domain.WorkflowNodeVariable;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author admin
 */
public interface WorkflowNodeVariableMapper extends BaseMapper<WorkflowNodeVariable> {
    /**
     * 批量保存节点变量列表
     *
     * @param workflowNodeVariableList 节点变量列表
     * @return 保存记录数
     */
    int batchInsert(@Param("workflowNodeVariableList") List<WorkflowNodeVariable> workflowNodeVariableList);
}