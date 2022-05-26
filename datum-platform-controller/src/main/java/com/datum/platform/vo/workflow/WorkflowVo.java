package com.datum.platform.vo.workflow;

import com.datum.platform.mapper.enums.WorkflowCreateModeEnum;
import com.datum.platform.service.dto.workflow.wizard.CalculationProcessStepDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "工作流列表响应对象")
public class WorkflowVo {

    @ApiModelProperty(value = "工作流ID")
    private Long workflowId;

    @ApiModelProperty(value = "工作流最新版本号")
    private Integer workflowVersion;

    @ApiModelProperty(value = "工作流名称")
    private String workflowName;

    @ApiModelProperty(value = "工作流算法名称")
    private String algorithmName;

    @ApiModelProperty(value = "工作流算法id")
    private Integer algorithmId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "最后运行时间")
    private Date lastRunTime;

    @ApiModelProperty(value = "是否设置完整")
    private Boolean isSettingCompleted;

    @ApiModelProperty(value = "创建模式（专家模型、向导模式）")
    private WorkflowCreateModeEnum createMode;

    @ApiModelProperty(value = "向导模式下当前步骤")
    private CalculationProcessStepDto calculationProcessStepObject;

    @ApiModelProperty(value = "计算流程id")
    private Long calculationProcessId;

    @ApiModelProperty(value = "向导模式下工作流计算流程名称")
    private String calculationProcessName;
}
