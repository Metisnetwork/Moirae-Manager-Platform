package com.platon.rosettaflow.req.workflow.node;

import com.platon.rosettaflow.common.constants.SysConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * 添加工作流节点资源请求对象
 * @author hudenian
 * @date 2021/8/31
 */
@Data
@ApiModel(value = "添加工作流节点资源请求对象")
public class SaveNodeResourceReq {

    @ApiModelProperty(value = "工作流节点ID", required = true)
    @NotNull(message = "{workflow.node.id.notNull}")
    @Positive(message = "{workflow.node.id.positive}")
    private Long workflowNodeId;

    @ApiModelProperty(value = "工作流节点资源内存", required = true)
    @NotNull(message = "{node.cost.memory.notNull}")
    private Long costMem;

    @ApiModelProperty(value = "工作流节点资源cpu", required = true)
    @NotNull(message = "{node.cost.cpu.notNull}")
    private Integer costCpu;

    @ApiModelProperty(value = "工作流节点资源带宽", required = true)
    @NotNull(message = "{node.cost.bandwidth.notNull}")
    private Long costBandwidth;

    @ApiModelProperty(value = "工作流节点运行时长(单位：h)", required = true)
    @NotNull(message = "{node.cost.runTime.notNull}")
    private Long runTime;

    /** 保存时处理内存单位 */
    public Long getCostMem() {
        return new BigDecimal(this.costMem)
                .multiply(BigDecimal.valueOf(SysConstant.INT_1024
                        * SysConstant.INT_1024 * SysConstant.INT_1024))
                .setScale(SysConstant.INT_0, BigDecimal.ROUND_UP)
                .longValue();
    }

    /** 保存时处理带宽单位 */
    public Long getCostBandwidth() {
        return new BigDecimal(this.costBandwidth)
                .multiply(BigDecimal.valueOf(SysConstant.INT_1000 * SysConstant.INT_1000))
                .setScale(SysConstant.INT_0, BigDecimal.ROUND_UP)
                .longValue();
    }

    /** 保存时最长运行时间单位处理 （换算为毫秒存库）*/
    public Long getRunTime() {
        return new BigDecimal(this.runTime)
                .multiply(BigDecimal.valueOf(SysConstant.INT_3600 * SysConstant.INT_1000))
                .setScale(SysConstant.INT_0, BigDecimal.ROUND_UP)
                .longValue();
    }

}
