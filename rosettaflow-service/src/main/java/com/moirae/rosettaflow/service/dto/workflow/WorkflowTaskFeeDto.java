package com.moirae.rosettaflow.service.dto.workflow;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "工作流列表响应对象")
public class WorkflowTaskFeeDto {

    @ApiModelProperty(value = "工作流任务配置id")
    private Long workflowTaskId;

    @ApiModelProperty(value = "费用明细")
    private List<WorkflowTaskFeeItemDto> itemList;
}
