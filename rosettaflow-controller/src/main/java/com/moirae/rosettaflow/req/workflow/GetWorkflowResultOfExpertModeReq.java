package com.moirae.rosettaflow.req.workflow;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * @author hudenian
 * @date 2021/9/16
 * @description 终止工作流
 */
@Data
@ApiModel
public class GetWorkflowResultOfExpertModeReq {

    @ApiModelProperty(value = "工作流节点id", required = true)
    @NotNull(message = "{workflow.id.notNull}")
    @Positive(message = "{workflow.id.positive}")
    private Long workflowNodeId;
}
