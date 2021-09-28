package com.platon.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platon.rosettaflow.mapper.domain.WorkflowNodeResource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author admin
 */
public interface WorkflowNodeResourceMapper extends BaseMapper<WorkflowNodeResource> {
    /**
     * 批量保存节点资源
     *
     * @param workflowNodeResourceList 节点资源列表
     * @return 保存记录数
     */
    int batchInsert(@Param("workflowNodeResourceList") List<WorkflowNodeResource> workflowNodeResourceList);
}