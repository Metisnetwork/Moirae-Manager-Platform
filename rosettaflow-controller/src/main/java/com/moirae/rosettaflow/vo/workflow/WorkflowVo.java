package com.moirae.rosettaflow.vo.workflow;

import com.moirae.rosettaflow.common.enums.CalculationProcessStepEnum;
import com.moirae.rosettaflow.common.enums.WorkflowCreateModeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "工作流列表响应对象")
public class WorkflowVo {

    @ApiModelProperty(value = "工作流ID")
    private Long id;

    @ApiModelProperty(value = "工作流名称")
    private String workflowName;

    @ApiModelProperty(value = "工作流算法名称")
    private String algorithmName;

    @ApiModelProperty(value = "工作流算法id")
    private Integer algorithmId;

    @ApiModelProperty(value = "工作流步骤")
    private String configAiStepName;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "最后运行时间")
    private Date lastRunTime;

    @ApiModelProperty(value = "是否设置完整")
    private Boolean isCompleted;

    @ApiModelProperty(value = "创建模式（专家模型、向导模式）")
    private WorkflowCreateModeEnum createMode;

    @ApiModelProperty(value = "向导模式下当前步骤")
    private CalculationProcessStepEnum curCalculationProcessStep;
}
