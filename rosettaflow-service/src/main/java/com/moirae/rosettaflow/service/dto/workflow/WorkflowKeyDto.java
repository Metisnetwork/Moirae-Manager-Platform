package com.moirae.rosettaflow.service.dto.workflow;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@ApiModel(value = "编辑工作流请求对象")
public class WorkflowKeyDto {

    @ApiModelProperty(value = "工作流ID", required = true)
    @NotNull(message = "{workflow.id.notNull}")
    @Positive(message = "{workflow.id.positive}")
    private Long workflowId;

}
