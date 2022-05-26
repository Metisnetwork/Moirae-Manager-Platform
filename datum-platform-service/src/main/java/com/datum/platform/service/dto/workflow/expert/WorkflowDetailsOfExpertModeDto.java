package com.datum.platform.service.dto.workflow.expert;

import com.datum.platform.mapper.enums.WorkflowTaskRunStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * 专家模式工作流详情
 */
@Data
@ApiModel(value = "专家模式工作流详情")
public class WorkflowDetailsOfExpertModeDto {

    @ApiModelProperty(value = "工作流ID", required = true)
    @NotNull(message = "{workflow.id.notNull}")
    @Positive(message = "{workflow.id.positive}")
    private Long workflowId;

    @ApiModelProperty(value = "工作流最新版本号")
    private Long workflowVersion;

    @ApiModelProperty(value = "工作流名称")
    private String workflowName;

    @ApiModelProperty(value = "是否设置完成:  0-否  1-是")
    private Boolean isSettingCompleted;

    @ApiModelProperty(value = "工作流版本执行状态（待运行、运行中、运行成功、运行失败）")
    private WorkflowTaskRunStatusEnum status;

    @ApiModelProperty(value = "工作流节点列表")
    List<NodeDto> workflowNodeList;
}
