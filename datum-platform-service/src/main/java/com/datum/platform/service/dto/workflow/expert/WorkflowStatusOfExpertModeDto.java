package com.datum.platform.service.dto.workflow.expert;

import com.datum.platform.mapper.enums.WorkflowTaskRunStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * 专家模式工作流的运行状态
 */
@Data
@ApiModel(value = "专家模式工作流的运行状态")
public class WorkflowStatusOfExpertModeDto {

    @ApiModelProperty(value = "工作流ID", required = true)
    @NotNull(message = "{workflow.id.notNull}")
    @Positive(message = "{workflow.id.positive}")
    private Long workflowId;

    @ApiModelProperty(value = "工作流最新版本号")
    private Long workflowVersion;

    @ApiModelProperty(value = "工作流运行状态:0-未运行,1-运行中,2-运行成功，3-运行失败")
    private WorkflowTaskRunStatusEnum runStatus;

    private List<NodeStatusDto> workflowNodeStatusList;
}
