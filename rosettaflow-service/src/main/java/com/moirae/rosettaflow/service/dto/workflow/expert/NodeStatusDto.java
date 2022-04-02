package com.moirae.rosettaflow.service.dto.workflow.expert;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 获取工作流运行状态响应参数
 * @author hudenian
 * @date 2021/8/30
 */
@Data
@ApiModel(value = "获取工作流运行状态响应参数")
public class NodeStatusDto {

    @ApiModelProperty(value = "工作流节点主键ID")
    private Long workflowNodeId;

    @ApiModelProperty(value = "工作流运行状态:0-未运行,1-运行中,2-运行成功，3-运行失败")
    private Integer runStatus;
}
