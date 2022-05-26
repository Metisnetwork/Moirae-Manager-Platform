package com.datum.platform.req.workflow.wizard;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class CreateWorkflowOfWizardModeReq {

    @ApiModelProperty(value = "工作流名称", required = true)
    @Length(min = 8, max = 64, message = "{workflow.name.Length}")
    @NotBlank(message = "{workflow.name.notBlank}")
    private String workflowName;

    @ApiModelProperty(value = "工作流描述", required = true)
    @Length(max = 200, message = "{workflow.desc.Length}")
    private String workflowDesc;

    @ApiModelProperty(value = "算法ID", required = true)
    @NotNull(message = "{algorithm.id.notNull}")
    private Long algorithmId;

    @ApiModelProperty(value = "计算流程id", required = true)
    @NotNull(message = "{algorithm.id.notNull}")
    private Long calculationProcessId;
}
