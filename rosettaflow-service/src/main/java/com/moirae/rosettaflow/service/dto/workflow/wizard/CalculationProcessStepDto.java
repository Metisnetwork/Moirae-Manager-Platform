package com.moirae.rosettaflow.service.dto.workflow.wizard;

import com.moirae.rosettaflow.mapper.enums.CalculationProcessTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class CalculationProcessStepDto {

    @ApiModelProperty(value = "步骤. 从1开始")
    private Integer step;

    @ApiModelProperty(value = "步骤说明. 0-选择训练输入数据, 1-选择预测输入数据, 2-选择PSI输入数据, 3-选择计算环境(通用), 4-选择计算环境(训练&预测), 5-选择结果接收方(通用), 6-选择结果接收方(训练&预测)", allowableValues = "0,1,2,3,4,5,6")
    private CalculationProcessTypeEnum type;
}
