package com.datum.platform.service.dto.workflow.expert;

import com.datum.platform.mapper.enums.WorkflowTaskRunStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 专家模式节点的运行状态
 */
@Data
@ApiModel(value = "专家模式节点的运行状态")
public class NodeStatusDto {

    @ApiModelProperty(value = "工作流节点主键ID")
    private Integer nodeStep;

    @ApiModelProperty(value = "工作流运行状态:0-未运行,1-运行中,2-运行成功，3-运行失败")
    private WorkflowTaskRunStatusEnum runStatus;
}
