package com.moirae.rosettaflow.vo.workflow;

import com.moirae.rosettaflow.service.dto.task.TaskEventDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class TaskEventVo extends TaskEventDto {

    @ApiModelProperty(value = "事件对应的任务id")
    private String taskId;
}
