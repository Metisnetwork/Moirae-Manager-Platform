package com.moirae.rosettaflow.req.workflow.expert;

import com.moirae.rosettaflow.common.constants.SysConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 添加工作流节点资源请求对象
 *
 * @author hudenian
 * @date 2021/9/28
 */
@Data
@ApiModel(value = "工作流节点资源请求对象")
public class WorkflowNodeResourceReq {

    @ApiModelProperty(value = "工作流节点资源内存 (单位: Mb)", required = true)
    @NotNull(message = "{node.cost.memory.notNull}")
    private Long costMem;

    @ApiModelProperty(value = "工作流节点资源cpu (单位: 个)", required = true)
    @NotNull(message = "{node.cost.cpu.notNull}")
    private Integer costCpu;

    @ApiModelProperty(value = "工作流节点资源gpu (单位：核)", required = true)
    @NotNull(message = "{node.cost.cpu.notNull}")
    private Integer costGpu;

    @ApiModelProperty(value = "工作流节点资源带宽 (单位: Mbps)", required = true)
    @NotNull(message = "{node.cost.bandwidth.notNull}")
    private Long costBandwidth;

    @ApiModelProperty(value = "工作流节点运行时长 (单位: 分钟)")
    private Long runTime;

    /**
     * 保存时处理内存单位
     */
    @SuppressWarnings("unused")
    public Long getCostMem() {
        return new BigDecimal(this.costMem)
                .multiply(BigDecimal.valueOf(SysConstant.INT_1024 * SysConstant.INT_1024))
                .setScale(SysConstant.INT_0, RoundingMode.UP)
                .longValue();
    }

    /**
     * 保存时处理带宽单位
     */
    @SuppressWarnings("unused")
    public Long getCostBandwidth() {
        return new BigDecimal(this.costBandwidth)
                .multiply(BigDecimal.valueOf(SysConstant.INT_1024 * SysConstant.INT_1024))
                .setScale(SysConstant.INT_0, RoundingMode.UP)
                .longValue();
    }

    /**
     * 保存时最长运行时间单位处理 （换算为毫秒存库）
     */
    public Long getRunTime() {
        return new BigDecimal(this.runTime)
                .multiply(BigDecimal.valueOf(SysConstant.INT_60 * SysConstant.INT_1000))
                .setScale(SysConstant.INT_0, RoundingMode.HALF_UP)
                .longValue();
    }

}
