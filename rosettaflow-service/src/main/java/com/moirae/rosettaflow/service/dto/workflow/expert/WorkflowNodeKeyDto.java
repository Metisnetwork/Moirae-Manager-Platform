package com.moirae.rosettaflow.service.dto.workflow.expert;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "工作流列表响应对象")
public class WorkflowNodeKeyDto {

    @ApiModelProperty(value = "工作流节点主键ID")
    private Long workflowNodeId;
}
