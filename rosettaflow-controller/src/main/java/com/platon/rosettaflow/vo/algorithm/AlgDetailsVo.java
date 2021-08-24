package com.platon.rosettaflow.vo.algorithm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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

    @ApiModelProperty(value = "算法代码")
    private String algorithmCode;
}
