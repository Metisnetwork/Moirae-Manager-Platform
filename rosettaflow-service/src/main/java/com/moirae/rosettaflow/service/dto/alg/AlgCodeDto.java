package com.moirae.rosettaflow.service.dto.alg;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * mo_algorithm_code
 *
 * @author admin
 */
@Data
@ApiModel(value = "算法代码")
public class AlgCodeDto {

    @ApiModelProperty(value = "编辑类型:1-sql,2-noteBook")
    private Integer editType;

    @ApiModelProperty(value = "计算合约")
    private String calculateContractCode;
}
