package com.moirae.rosettaflow.req.home;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "获得最新的任务列表请求")
public class GetLatestTaskListReq {
    @ApiModelProperty(value = "返回数量, 默认10条.")
    private Integer size = 10;
}
