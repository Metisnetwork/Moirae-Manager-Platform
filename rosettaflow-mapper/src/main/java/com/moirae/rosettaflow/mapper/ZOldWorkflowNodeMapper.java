package com.moirae.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moirae.rosettaflow.mapper.domain.ZOldWorkflowNode;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author admin
 * @date 2021/8/16
 */
public interface ZOldWorkflowNodeMapper extends BaseMapper<ZOldWorkflowNode> {

    List<ZOldWorkflowNode> queryWorkflowNodeAndStatusList(@Param("workflowId") Long workflowId, @Param("editVersion") Integer editVersion);

    List<ZOldWorkflowNode> queryWorkflowDetailAndSpecifyStatus(@Param("workflowRunStatusId") Long workflowRunStatusId);
}
