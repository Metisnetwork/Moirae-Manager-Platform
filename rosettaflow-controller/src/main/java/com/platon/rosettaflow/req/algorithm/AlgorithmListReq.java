package com.platon.rosettaflow.req.algorithm;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author houz
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "算法列表请求参数")
public class AlgorithmListReq {

    @ApiModelProperty(value = "用户ID", example = "", required = true)
    @NotNull(message = "用户ID不能为空")
    private Long userId;

}
