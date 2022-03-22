package com.moirae.rosettaflow.vo.workflow;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "数据统计")
public class WorkflowStatsVo {

    @ApiModelProperty(value = "工作流的总数(用户的)")
    private int workflowCount;
}
