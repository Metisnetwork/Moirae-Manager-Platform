package com.moirae.rosettaflow.service.dto.workflow.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "工作流节点输入变量请求对象")
public class VariableDto {

    @ApiModelProperty(value = "变量key", required = true)
    private String varKey;

    @ApiModelProperty(value = "变量值", required = true)
    private String varValue;

    @ApiModelProperty(value = "变量描述")
    private String varDesc;

    @ApiModelProperty(value = "变量描述")
    private String varDescEn;

}
