package com.moirae.rosettaflow.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moirae.rosettaflow.mapper.domain.WorkflowNodeTemp;

/**
 * 工作流节点模板
 * @author houz
 */
public interface WorkflowNodeTempMapper extends BaseMapper<WorkflowNodeTemp> {
    void truncate();
}