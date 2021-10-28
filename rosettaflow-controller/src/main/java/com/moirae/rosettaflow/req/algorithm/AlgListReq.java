package com.moirae.rosettaflow.req.algorithm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author houz
 */
@Data
@ApiModel(value = "算法列表请求参数")
public class AlgListReq {

    @ApiModelProperty(value = "算法名称")
    private String algorithmName;

}
