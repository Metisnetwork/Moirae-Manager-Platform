package com.datum.platform.service.dto.workflow.wizard;

import com.datum.platform.service.dto.workflow.common.OutputDto;
import com.datum.platform.service.dto.workflow.common.ResourceDto;
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
    private Long workflowVersion;

    @ApiModelProperty(value = "工作流名称")
    private String workflowName;

    @ApiModelProperty(value = "工作流描述")
    private String workflowDesc;

    @ApiModelProperty(value = "计算流程的已完成的步骤，如果还未设置返回0")
    private Integer completedCalculationProcessStep;

    @ApiModelProperty(value = "是否设置完成:  0-否  1-是")
    private Boolean isSettingCompleted;

    @ApiModelProperty(value = "工作流算法id")
    private Long algorithmId;

    @ApiModelProperty(value = "计算流程id")
    private Long calculationProcessId;

    @ApiModelProperty(value = "向导模式下计算流程的当前步骤")
    private CalculationProcessStepDto calculationProcessStep;

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

    @ApiModelProperty(value = "7-选择明文算法训练输入数据")
    private PTTrainingInputDto ptTrainingInput;

    @ApiModelProperty(value = "8-选择明文算法预测输入数据")
    private PTPredictionInputDto ptPredictionInput;
}
