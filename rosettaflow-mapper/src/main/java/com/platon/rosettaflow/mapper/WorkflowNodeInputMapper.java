package com.platon.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platon.rosettaflow.mapper.domain.WorkflowNodeInput;

import java.util.List;

/**
 * @author admin
 */
public interface WorkflowNodeInputMapper extends BaseMapper<WorkflowNodeInput> {

    /**
     * 查询工作流节点输入相关数据
     * @param idList
     * @return
     */
    List<WorkflowNodeInput> queryWorkflowNodeRelatedData(List<Long> idList);
}