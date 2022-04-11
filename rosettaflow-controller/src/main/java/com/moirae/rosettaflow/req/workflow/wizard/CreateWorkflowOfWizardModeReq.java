package com.moirae.rosettaflow.req.workflow.wizard;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@ApiModel
public class CreateWorkflowOfWizardModeReq {

    @ApiModelProperty(value = "工作流名称", required = true)
    @Length(max = 30, message = "{workflow.name.Length}")
    private String workflowName;

    @ApiModelProperty(value = "工作流描述", required = true)
    @Length(max = 200, message = "{workflow.desc.Length}")
    private String workflowDesc;

    @ApiModelProperty(value = "算法ID", required = true)
    private Long algorithmId;

    @ApiModelProperty(value = "计算流程id", required = true)
    private Long calculationProcessId;
}
