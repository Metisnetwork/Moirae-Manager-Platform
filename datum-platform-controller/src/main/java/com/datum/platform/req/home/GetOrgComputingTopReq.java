package com.datum.platform.req.home;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "获得最新的模型列表")
public class GetOrgComputingTopReq {
    @ApiModelProperty(value = "返回天数, 默认15条.")
    private Integer size = 20;
}
