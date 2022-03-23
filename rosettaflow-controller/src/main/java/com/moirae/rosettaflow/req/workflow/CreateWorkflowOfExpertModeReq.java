package com.moirae.rosettaflow.req.workflow;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@ApiModel
public class CreateWorkflowOfExpertModeReq {

    @ApiModelProperty(value = "工作流名称", required = true)
    @Length(max = 30, message = "{workflow.name.Length}")
    private String workflowName;

}
