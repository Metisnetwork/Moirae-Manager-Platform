package com.moirae.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moirae.rosettaflow.mapper.domain.WorkflowRunStatus;
import org.apache.ibatis.annotations.Param;

/**
 * @author admin
 * @date 2021/8/16
 */
public interface WorkflowRunStatusMapper extends BaseMapper<WorkflowRunStatus> {
    IPage<WorkflowRunStatus> runningRecordList(@Param("userId") Long userId, @Param("projectId") Long projectId, @Param("workflowName") String workflowName, IPage<WorkflowRunStatus> page);
}
