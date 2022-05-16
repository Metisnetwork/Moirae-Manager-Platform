package com.moirae.rosettaflow.req.workflow.expert;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel
public class CreateWorkflowOfExpertModeReq {

    @ApiModelProperty(value = "工作流名称", required = true)
    @Length(min = 8, max = 64, message = "{workflow.name.Length}")
    @NotBlank(message = "{workflow.name.notBlank}")
    private String workflowName;
}
