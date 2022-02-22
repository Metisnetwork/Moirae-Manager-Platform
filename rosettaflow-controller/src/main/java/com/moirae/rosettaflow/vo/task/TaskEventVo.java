package com.moirae.rosettaflow.vo.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "任务事件")
public class TaskEventVo {

    @ApiModelProperty(value = "任务ID,hash")
    private String taskId;

    @ApiModelProperty(value = "事件类型")
    private String eventType;

    @ApiModelProperty(value = "产生事件的组织身份ID")
    private String identityId;

    @ApiModelProperty(value = "产生事件的组织身份ID")
    private String partyId;

    @ApiModelProperty(value = "产生事件的时间")
    private Date eventAt;

    @ApiModelProperty(value = "事件内容")
    private String eventContent;
}
