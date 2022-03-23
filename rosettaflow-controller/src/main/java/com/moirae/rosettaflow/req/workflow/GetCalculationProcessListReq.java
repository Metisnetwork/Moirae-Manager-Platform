package com.moirae.rosettaflow.req.workflow;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "任务列表查询")
public class GetCalculationProcessListReq {

    @ApiModelProperty(value = "算法ID（向导模式）", required = true)
    private Long algorithmId;
}
