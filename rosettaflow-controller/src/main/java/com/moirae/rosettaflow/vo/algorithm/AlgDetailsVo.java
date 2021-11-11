package com.moirae.rosettaflow.vo.algorithm;

import com.moirae.rosettaflow.common.constants.SysConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author houz
 */
@Data
@ApiModel(value = "查询算法详情响应参数")
public class AlgDetailsVo {

    @ApiModelProperty(value = "算法名称")
    private String algorithmName;

    @ApiModelProperty(value = "算法描述")
    private String algorithmDesc;

    @ApiModelProperty(value = "作者")
    private String author;

    @ApiModelProperty(value = "支持协同方最大数量")
    private Long maxNumbers;

    @ApiModelProperty(value = "支持协同方最小数量")
    private Long minNumbers;

    @ApiModelProperty(value = "支持语言,多个以','进行分隔")
    private String supportLanguage;

    @ApiModelProperty(value = "支持操作系统,多个以','进行分隔")
    private String supportOsSystem;

    @ApiModelProperty(value = "算法所属大类:1-统计分析,2-特征工程,3-机器学习")
    private Byte algorithmType;

    @ApiModelProperty(value = "算法所属大类:1-统计分析,2-特征工程,3-机器学习")
    private String algorithmTypeDesc;

    @ApiModelProperty(value = "所需的内存 (单位: byte)")
    private Long costMem;

    @ApiModelProperty(value = "所需的核数 (单位: 个)")
    private Long costCpu;

    @ApiModelProperty(value = "GPU核数(单位：核)")
    private Integer costGpu;

    @ApiModelProperty(value = "所需的带宽 (单位: bps)")
    private Long costBandwidth;

    @ApiModelProperty(value = "所需的运行时长 (单位: ms)")
    private Long runTime;

    @ApiModelProperty(value = "是否需要模型: 0-否，1:是")
    private Byte inputModel;

    @ApiModelProperty(value = "输出存储形式: 1-明文，2:密文")
    private Byte storePattern;

    @ApiModelProperty(value = "代码编辑类型")
    private String editType;

    @ApiModelProperty(value = "算法代码")
    private String calculateContractCode;

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
                .divide(BigDecimal.valueOf(SysConstant.INT_1024 * SysConstant.INT_1024),
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
