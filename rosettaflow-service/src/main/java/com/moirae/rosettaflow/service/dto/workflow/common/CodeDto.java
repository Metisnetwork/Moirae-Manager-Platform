package com.moirae.rosettaflow.service.dto.workflow.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 工作流算法代码
 */
@Data
@ApiModel(value = "工作流算法代码明细")
public class CodeDto {

    @ApiModelProperty(value = "编辑类型:1-sql, 2-noteBook", required = true)
    @NotNull(message = "{workflow.node.algorithm.type.notNull}")
    private Integer editType;

    @ApiModelProperty(value = "算法代码", required = true)
    @NotBlank(message = "{workflow.node.algorithm.code.NotBlank}")
    private String calculateContractCode;

    @ApiModelProperty(value = "算法数据分片")
    private String dataSplitContractCode;

}
