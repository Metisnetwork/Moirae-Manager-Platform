package com.platon.rosettaflow.req.workflow.node;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * 添加工作流节点资源请求对象
 * @author hudenian
 * @date 2021/8/31
 */
@Data
@ApiModel(value = "添加工作流节点资源请求对象")
public class SaveNodeResourceReq {

    @ApiModelProperty(value = "工作流ID", required = true)
    @NotNull(message = "{workflow.node.id.notNull}")
    @Positive(message = "{workflow.node.id.positive}")
    private Long workflowNodeId;

    @ApiModelProperty(value = "工作流节点资源内存", required = true)
    @NotNull(message = "{node.cost.memory.notNull}")
    private Long costMem;

    @ApiModelProperty(value = "工作流节点资源cpu", required = true)
    @NotNull(message = "{node.cost.cpu.notNull}")
    private Integer costCpu;

    @ApiModelProperty(value = "工作流节点资源gpu", required = true)
    @NotBlank(message = "{node.cost.cpu.notNull}")
    private Integer costGpu;

    @ApiModelProperty(value = "工作流节点资源带宽", required = true)
    @NotBlank(message = "{node.cost.bandwidth.notNull}")
    private Long costBandwidth;

    @ApiModelProperty(value = "工作流节点运行时长")
    private Long runTime;

}
