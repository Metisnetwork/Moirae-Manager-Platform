package com.moirae.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moirae.rosettaflow.mapper.domain.WorkflowNode;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author admin
 * @date 2021/8/16
 */
public interface WorkflowNodeMapper extends BaseMapper<WorkflowNode> {

    List<WorkflowNode> queryWorkflowNodeAndStatusList(@Param("workflowId") Long workflowId,  @Param("editVersion") Integer editVersion);

    List<WorkflowNode> queryWorkflowDetailAndSpecifyStatus(@Param("workflowRunStatusId") Long workflowRunStatusId);
}
