package com.moirae.rosettaflow.service.dto.workflow.wizard;

import com.moirae.rosettaflow.mapper.domain.CalculationProcessStep;
import com.moirae.rosettaflow.service.dto.workflow.common.OutputDto;
import com.moirae.rosettaflow.service.dto.workflow.common.ResourceDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@ApiModel
public class WorkflowDetailsOfWizardModeDto {

    @ApiModelProperty(value = "工作流ID", required = true)
    @NotNull(message = "{workflow.id.notNull}")
    @Positive(message = "{workflow.id.positive}")
    private Long workflowId;

    @ApiModelProperty(value = "工作流最新版本号")
    private Integer workflowVersion;

    @ApiModelProperty(value = "计算流程的当前步骤")
    private CalculationProcessStep calculationProcessStep;


    @ApiModelProperty(value = "0-选择训练输入数据")
    private TrainingInputDto trainingInput;

    @ApiModelProperty(value = "1-选择预测输入数据")
    private PredictionInputDto predictionInput;

    @ApiModelProperty(value = "2-选择PSI输入数据")
    private PsiInputDto psiInput;

    @ApiModelProperty(value = "3-选择计算环境(通用)")
    private ResourceDto commonResource;

    @ApiModelProperty(value = "4-选择计算环境(训练&预测)")
    private TrainingAndPredictionResourceDto trainingAndPredictionResource;

    @ApiModelProperty(value = "5-选择结果接收方(通用)")
    private OutputDto commonOutput;

    @ApiModelProperty(value = "6-选择结果接收方(训练&预测)")
    private TrainingAndPredictionOutputDto trainingAndPredictionOutput;
}
