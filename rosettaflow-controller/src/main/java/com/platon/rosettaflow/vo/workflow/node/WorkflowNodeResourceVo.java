package com.platon.rosettaflow.vo.workflow.node;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 工作流节点资源环境响应参数
 * @author hudenian
 * @date 2021/8/30
 */
@Data
@ApiModel(value = "工作流节点资源环境响应参数")
public class WorkflowNodeResourceVo {

    @ApiModelProperty(value = "节点资源表主键ID")
    private Long id;

    @ApiModelProperty(value = "工作流节点主键id")
    private Long workflowNodeId;

    @ApiModelProperty(value = "所需的内存 (单位: byte)")
    private Long costMem;

    @ApiModelProperty(value = "所需的核数 (单位: 个)")
    private Long costProcessor;

    @ApiModelProperty(value = "GPU核数(单位：核)")
    private Integer costGpu;

    @ApiModelProperty(value = "所需的带宽 (单位: bps)")
    private Long costBandwidth;

    @ApiModelProperty(value = "所需的运行时长 (单位: ms)")
    private Long duration;

    @ApiModelProperty(value = "状态: 0-无效，1- 有效")
    private Byte status;
}
