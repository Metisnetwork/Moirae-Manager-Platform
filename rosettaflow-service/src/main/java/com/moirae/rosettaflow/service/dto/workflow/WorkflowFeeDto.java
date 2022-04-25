package com.moirae.rosettaflow.service.dto.workflow;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "工作流列表响应对象")
public class WorkflowFeeDto extends WorkflowVersionKeyDto {

    @ApiModelProperty(value = "工作流任务费用明细")
    private List<WorkflowFeeItemDto> itemList;
}
