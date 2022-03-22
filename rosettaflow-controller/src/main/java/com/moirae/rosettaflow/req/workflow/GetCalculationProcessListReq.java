package com.moirae.rosettaflow.req.workflow;

import com.moirae.rosettaflow.req.CommonPageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@ApiModel(value = "任务列表查询")
public class GetCalculationProcessListReq {

    @ApiModelProperty(value = "算法ID（向导模式）", required = true)
    private Long algorithmId;
}
