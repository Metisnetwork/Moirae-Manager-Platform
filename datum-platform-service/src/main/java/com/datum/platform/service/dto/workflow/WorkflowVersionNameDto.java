package com.datum.platform.service.dto.workflow;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "工作流版本名称")
public class WorkflowVersionNameDto extends WorkflowVersionKeyDto {

    @ApiModelProperty(value = "工作流版本名称")
    private String workflowVersionName;
}
