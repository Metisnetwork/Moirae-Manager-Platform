package com.datum.platform.req.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "任务查询")
public class GetTaskDetailsReq {

    @ApiModelProperty(value = "任务Id", required = true)
    @NotBlank(message = "{taskresult.id.notNull}")
    private String taskId;
}
