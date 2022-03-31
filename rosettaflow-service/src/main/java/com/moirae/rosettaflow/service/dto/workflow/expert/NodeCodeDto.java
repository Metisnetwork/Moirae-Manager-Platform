package com.moirae.rosettaflow.service.dto.workflow.expert;

import com.moirae.rosettaflow.service.dto.workflow.common.CodeDto;
import com.moirae.rosettaflow.service.dto.workflow.common.VariableDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "工作流详情请求对象")
public class NodeCodeDto {

    @ApiModelProperty(value = "变量", required = true)
    private List<VariableDto> variableList;

    @ApiModelProperty(value = "代码", required = true)
    private CodeDto code;
}
