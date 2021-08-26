package com.platon.rosettaflow.req.algorithm;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 算法详情请求参数
 * @author houz
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "算法详情请求参数")
public class AlgDetailsReq {

    @ApiModelProperty(value = "算法ID", example = "", required = true)
    @NotNull(message = "算法ID不能为空")
    private Long id;

}
