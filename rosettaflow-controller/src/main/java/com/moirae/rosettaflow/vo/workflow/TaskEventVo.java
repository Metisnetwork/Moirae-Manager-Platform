package com.moirae.rosettaflow.vo.workflow;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class TaskEventVo extends com.moirae.rosettaflow.vo.task.TaskEventVo {

    @ApiModelProperty(value = "事件对应的任务id")
    private String taskId;
}
