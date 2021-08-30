package com.platon.rosettaflow.vo.workflow;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hudenian
 * @date 2021/8/30
 * @description 功能描述
 */
@Data
@ApiModel
public class WorkflowNodeVariableVo {

    @ApiModelProperty(value = "工作流节点输入变量表ID")
    private Long id;

    @ApiModelProperty(value = "工作流节点id")
    private Long workflowNodeId;

    @ApiModelProperty(value = "变量类型: 1-自变量, 2-因变量")
    private Byte varNodeType;

    @ApiModelProperty(value = "变量key")
    private String varNodeKey;

    @ApiModelProperty(value = "变量值")
    private String varNodeValue;

    @ApiModelProperty(value = "变量描述")
    private String varNodeDesc;

    @ApiModelProperty(value = "状态: 0-无效，1- 有效")
    private Byte status;
}
