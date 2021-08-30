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
