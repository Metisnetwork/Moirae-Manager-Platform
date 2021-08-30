package com.platon.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.platon.rosettaflow.dto.UserMetaDataDto;
import com.platon.rosettaflow.dto.WorkflowDto;
import com.platon.rosettaflow.mapper.domain.UserMetaData;
import com.platon.rosettaflow.mapper.domain.Workflow;
import org.apache.ibatis.annotations.Param;

/**
 * @author admin
 * @date 2021/8/16
 */
public interface WorkflowMapper extends BaseMapper<Workflow> {

    IPage<WorkflowDto> listByProjectId(@Param("page") IPage<Workflow> page, @Param("projectId") Long projectId, @Param("workflowName") String workflowName);

}