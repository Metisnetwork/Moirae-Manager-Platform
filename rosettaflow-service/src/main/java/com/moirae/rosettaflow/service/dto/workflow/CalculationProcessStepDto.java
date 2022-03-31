package com.moirae.rosettaflow.service.dto.workflow;

import com.moirae.rosettaflow.common.enums.CalculationProcessStepEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(value = "工作流详情请求对象")
public class CalculationProcessStepDto {

    @ApiModelProperty(value = "计算流程ID")
    private Long calculationProcessId;

    @ApiModelProperty(value = "步骤. 从0开始")
    private Integer step;

    @ApiModelProperty(value = "部署说明. 0-选择训练输入数据, 1-选择预测输入数据, 2-选择PSI输入数据, 3-选择计算环境(通用), 4-选择计算环境(训练&预测), 5-选择结果接收方(通用), 6-选择结果接收方(训练&预测)")
    private CalculationProcessStepEnum type;
}
