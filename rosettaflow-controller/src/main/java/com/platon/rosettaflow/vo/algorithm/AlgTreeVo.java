package com.platon.rosettaflow.vo.algorithm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author houz
 */
@Data
@ApiModel(value = "查询算法树详情响应参数")
public class AlgTreeVo {

    @ApiModelProperty(value = "算法ID")
    private Long algorithmId;

    @ApiModelProperty(value = "算法名称")
    private String algorithmName;

    @ApiModelProperty(value = "算法详情")
    private List<AlgChildTreeVo> child;

}
