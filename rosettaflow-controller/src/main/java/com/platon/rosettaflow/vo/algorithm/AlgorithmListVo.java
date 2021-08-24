package com.platon.rosettaflow.vo.algorithm;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author houz
 */
@Data
@ApiModel("算法列表响应参数")
public class AlgorithmListVo {

    @ApiModelProperty(value = "算法名称", example = "", required = true)
    private String algorithmName;

    @ApiModelProperty(value = "算法描述", example = "", required = true)
    private String algorithmDesc;

    @ApiModelProperty(value = "算法类型", example = "", required = true)
    private String algorithmType;

}
