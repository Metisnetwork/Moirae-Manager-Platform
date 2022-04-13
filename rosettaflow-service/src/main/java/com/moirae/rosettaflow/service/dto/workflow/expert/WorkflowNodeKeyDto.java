package com.moirae.rosettaflow.service.dto.workflow.expert;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 专家模式节点id
 */
@Data
@ApiModel(value = "专家模式节点id")
public class WorkflowNodeKeyDto {

    @ApiModelProperty(value = "工作流节点主键ID")
    private Long workflowNodeId;
}
