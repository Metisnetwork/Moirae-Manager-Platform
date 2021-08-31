package com.platon.rosettaflow.req.workflownode;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * @author hudenian
 * @date 2021/8/31
 * @description 添加工作流节点请求对象
 */
@Data
@ApiModel
public class AddWorkflowNodeReq {

    @ApiModelProperty(value = "工作流ID", required = true)
    @NotNull(message = "{workflow.id.notNull}")
    @Positive(message = "{workflow.id.positive}")
    private Long workflowId;

    @ApiModelProperty(value = "算法ID", required = true)
    @NotNull(message = "算法ID不能为空")
    @Positive(message = "算法ID错误")
    private Long algorithmId;

    @ApiModelProperty(value = " 节点在工作流中序号,从1开始", required = true)
    @NotNull(message = " 节点在工作流中序号不能为空")
    @Positive(message = " 节点在工作流中序号错误")
    private Integer nodeStep;

}
