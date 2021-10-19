package com.platon.rosettaflow.vo.algorithm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author houz
 */
@Data
@ApiModel(value = "算法树详情响应参数")
public class AlgChildTreeVo {

    @ApiModelProperty(value = "算法ID")
    private Long algorithmId;

    @ApiModelProperty(value = "算法名称")
    private String algorithmName;

    @ApiModelProperty(value = "算法详情")
    private AlgDetailsVo algDetailsVo;

}
