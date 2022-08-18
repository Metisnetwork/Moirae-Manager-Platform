package com.datum.platform.service.dto.workflow.wizard;

import com.datum.platform.mapper.enums.CalculationProcessTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@ApiModel
public class CalculationProcessStepDto {

    @ApiModelProperty(value = "步骤. 从1开始")
    @NotNull(message = "{calculation.process.step.object.step.notNull}")
    @Positive(message = "{calculation.process.step.object.step.positive}")
    private Integer step;

    @ApiModelProperty(value = "步骤说明. 0-选择训练输入数据, 1-选择预测输入数据, 2-选择PSI输入数据, 3-选择计算环境(通用), 4-选择计算环境(训练&预测), 5-选择结果接收方(通用), 6-选择结果接收方(训练&预测), 7-选择明文训练输入数据, 8-选择明文预测输入数据", allowableValues = "0,1,2,3,4,5,6,7,8")
    @NotNull(message = "{calculation.process.step.object.type.notNull}")
    private CalculationProcessTypeEnum type;
}
