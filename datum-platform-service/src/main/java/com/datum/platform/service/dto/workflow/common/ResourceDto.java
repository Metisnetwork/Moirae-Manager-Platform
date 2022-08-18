package com.datum.platform.service.dto.workflow.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 工作流资源明细
 */
@Data
@ApiModel
public class ResourceDto {

    @ApiModelProperty(value = "工作流节点资源内存 (单位: Mb)", required = true)
    @NotNull(message = "{task.cost.memory.notNull}")
    private Long costMem;

    @ApiModelProperty(value = "工作流节点资源cpu (单位: 个)", required = true)
    @NotNull(message = "{task.cost.cpu.notNull}")
    private Integer costCpu;

    @ApiModelProperty(value = "工作流节点资源gpu (单位：核)", required = true)
    private Integer costGpu;

    @ApiModelProperty(value = "工作流节点资源带宽 (单位: Mbps)", required = true)
    @NotNull(message = "{task.cost.bandwidth.notNull}")
    private Long costBandwidth;

    @ApiModelProperty(value = "工作流节点运行时长 (单位: 分钟)")
    @NotNull(message = "{task.cost.runTime.notNull}")
    private Long runTime;
}
