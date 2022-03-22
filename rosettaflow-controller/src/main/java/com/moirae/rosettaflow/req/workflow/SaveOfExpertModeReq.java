package com.moirae.rosettaflow.req.workflow;

import com.moirae.rosettaflow.common.enums.CalculationProcessStepEnum;
import com.moirae.rosettaflow.req.workflow.step.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@ApiModel
public class SaveOfExpertModeReq {

    @ApiModelProperty(value = "工作流ID", required = true)
    @NotNull(message = "{workflow.id.notNull}")
    @Positive(message = "{workflow.id.positive}")
    private Long workflowId;

    @ApiModelProperty(value = "向导模式下当前步骤", required = true)
    private CalculationProcessStepEnum curCalculationProcessStep;

    @ApiModelProperty(value = "选择训练输入数据")
    private TrainingInputReq trainingInput;

    @ApiModelProperty(value = "选择预测输入数据")
    private PredictionInputReq predictionInput;

    @ApiModelProperty(value = "选择PSI输入数据")
    private PsiInputReq psiInput;

    @ApiModelProperty(value = "选择计算环境")
    private ResourceReq resource;

    @ApiModelProperty(value = "选择结果接收方")
    private OutputReq output;
}
