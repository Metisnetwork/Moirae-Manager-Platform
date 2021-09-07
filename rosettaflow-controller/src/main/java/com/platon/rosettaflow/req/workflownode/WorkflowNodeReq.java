package com.platon.rosettaflow.req.workflownode;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * 保存工作流节点请求对象
 * @author hudenian
 * @date 2021/8/31
 */
@Data
@ApiModel
public class WorkflowNodeReq {

    @ApiModelProperty(value = "工作流ID", required = true)
    @NotNull(message = "{workflow.node.id.notNull}")
    @Positive(message = "{workflow.node.id.positive}")
    private Long id;

    @ApiModelProperty(value = "工作流当前节点序号,从1开始", required = true)
    @NotNull(message = "{workflow.node.step.notNull}")
    @Positive(message = "{workflow.node.step.positive}")
    private Integer nodeStep;

}
