package com.moirae.rosettaflow.req.workflow;

import com.moirae.rosettaflow.common.enums.CalculationProcessStepEnum;
import com.moirae.rosettaflow.req.workflow.wizard.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@ApiModel
public class SettingWorkflowOfWizardModeReq {

    @ApiModelProperty(value = "工作流ID", required = true)
    @NotNull(message = "{workflow.id.notNull}")
    @Positive(message = "{workflow.id.positive}")
    private Long workflowId;

    @ApiModelProperty(value = "工作流最新版本号")
    private Integer workflowVersion;

    @ApiModelProperty(value = "计算流程的设置步骤")
    private CalculationProcessStepEnum calculationProcessStep;

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
