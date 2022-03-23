package com.moirae.rosettaflow.req.workflow.expert;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 工作流节点变量请求对象
 *
 * @author hudenian
 * @date 2021/9/28
 */
@Data
@ApiModel(value = "工作流节点输入变量请求对象")
public class WorkflowNodeVariableReq {

    @ApiModelProperty(value = "变量类型: 1-自变量, 2-因变量", required = true)
    private Byte varNodeType;

    @ApiModelProperty(value = "变量key", required = true)
    private String varNodeKey;

    @ApiModelProperty(value = "变量值", required = true)
    private String varNodeValue;

    @ApiModelProperty(value = "变量描述")
    private String varNodeDesc;

}
