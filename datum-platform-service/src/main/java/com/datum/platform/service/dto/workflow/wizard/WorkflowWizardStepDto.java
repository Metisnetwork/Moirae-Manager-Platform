package com.datum.platform.service.dto.workflow.wizard;

import com.datum.platform.service.dto.workflow.WorkflowVersionKeyDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "工作流向导步骤")
public class WorkflowWizardStepDto extends WorkflowVersionKeyDto {

    @ApiModelProperty(value = "步骤变化")
    private Integer step;
}
