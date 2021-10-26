package com.platon.rosettaflow.vo.workflow.node;

import com.platon.rosettaflow.common.constants.SysConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 工作流节点代码响应参数
 * @author hudenian
 * @date 2021/8/30
 */
@Data
@ApiModel(value = "工作流节点代码响应参数")
public class NodeAlgorithmVo {

    @ApiModelProperty(value = "工作流节点表主键ID")
    private Long workflowNodeId;

    @ApiModelProperty(value = "工作流节点运行状态")
    private Byte runStatus;

    @ApiModelProperty(value = "算法id")
    private Long algorithmId;

    @ApiModelProperty(value = "算法名称")
    private String algorithmName;

    @ApiModelProperty(value = "算法描述")
    private String algorithmDesc;

    @ApiModelProperty(value = "算法作者")
    private String author;

    @ApiModelProperty(value = "支持协同方最大数量")
    private Long maxNumbers;

    @ApiModelProperty(value = "支持协同方最小数量")
    private Long minNumbers;

    @ApiModelProperty(value = "支持语言,多个以\",\"进行分隔")
    private String supportLanguage;

    @ApiModelProperty(value = "算法编辑类型:1-sql, 2-noteBook")
    private Byte editType;

    @ApiModelProperty(value = "算法代码（计算合约）")
    private String calculateContractCode;

    @ApiModelProperty(value = "所需的内存 (单位: byte)")
    private Long costMem;

    @ApiModelProperty(value = "所需的核数 (单位: 个)")
    private Integer costCpu;

    @ApiModelProperty(value = "所需的带宽 (单位: bps)")
    private Long costBandwidth;

    @ApiModelProperty(value = "所需的运行时长 (单位: h)")
    private Long runTime;

    @ApiModelProperty(value = "输出存储形式: 1-明文，2:密文")
    private Byte storePattern;



    /** 展示时处理内存单位 */
    public Long getCostMem() {
        if (null == this.costMem || this.costMem == 0) {
            return 0L;
        }
        return new BigDecimal(this.costMem)
                .divide(BigDecimal.valueOf(SysConstant.INT_1024
                                * SysConstant.INT_1024 * SysConstant.INT_1024),
                        SysConstant.INT_0, RoundingMode.UP).longValue();
    }

    /** 展示时处理带宽单位 */
    public Long getCostBandwidth() {
        if (null == this.costBandwidth || this.costBandwidth == 0) {
            return 0L;
        }
        return new BigDecimal(this.costBandwidth)
                .divide(BigDecimal.valueOf(SysConstant.INT_1000 * SysConstant.INT_1000),
                        SysConstant.INT_0, RoundingMode.UP).longValue();
    }

    /** 展示时最长运行时间单位处理 */
    public Long getRunTime() {
        if (null == this.runTime || this.runTime == 0) {
            return 0L;
        }
        return new BigDecimal(this.runTime)
                .divide(BigDecimal.valueOf(SysConstant.INT_60 * SysConstant.INT_1000),
                        SysConstant.INT_0, RoundingMode.HALF_UP).longValue();
    }

}
