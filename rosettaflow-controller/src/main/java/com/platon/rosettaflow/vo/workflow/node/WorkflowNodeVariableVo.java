package com.platon.rosettaflow.vo.workflow.node;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 工作流节点变量响应参数
 * @author hudenian
 * @date 2021/8/30
 */
@Data
@ApiModel(value = "工作流节点变量响应参数")
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
}
