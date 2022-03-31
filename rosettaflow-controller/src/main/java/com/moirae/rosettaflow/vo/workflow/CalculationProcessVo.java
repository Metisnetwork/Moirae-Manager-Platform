package com.moirae.rosettaflow.vo.workflow;

import com.moirae.rosettaflow.common.enums.CalculationProcessStepEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "工作流列表响应对象")
public class CalculationProcessVo {

    @ApiModelProperty(value = "配置ID")
    private Long calculationProcessId;

    @ApiModelProperty(value = "配置名称")
    private String name;

    @ApiModelProperty(value = "计算步骤")
    private List<CalculationProcessStepEnum> stepItem;
}
