package com.moirae.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moirae.rosettaflow.dto.WorkflowDto;
import com.moirae.rosettaflow.mapper.domain.WorkflowNode;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author admin
 * @date 2021/8/16
 */
public interface WorkflowNodeMapper extends BaseMapper<WorkflowNode> {

    List<WorkflowNode> queryWorkflowNodeAndStatusList(@Param("workflowId") Long workflowId,  @Param("editVersion") Long editVersion);
}
