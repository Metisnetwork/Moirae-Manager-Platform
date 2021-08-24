package com.platon.rosettaflow.req.algorithm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author houz
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "算法列表请求参数")
public class AlgorithmReq {

    @ApiModelProperty(value = "算法ID", example = "", required = true)
    @NotNull(message = "算法ID不能为空")
    private Long id;

    @ApiModelProperty(value = "算法名称", example = "", required = true)
    @NotNull(message = "算法名称不能为空")
    private String algorithmName;

    @ApiModelProperty(value = "算法描述", example = "")
    private String algorithmDesc;

    @ApiModelProperty(value = "支持协同方最大数量", example = "", required = true)
    @NotNull(message = "支持协同方最大数量不能为空")
    private Long maxNumbers;

    @ApiModelProperty(value = "支持协同方最小数量", example = "", required = true)
    @NotNull(message = "支持协同方最小数量不能为空")
    private Long minNumbers;

    @ApiModelProperty(value = "支持语言,多个以','进行分隔", example = "")
    private String supportLanguage;

    @ApiModelProperty(value = "支持操作系统,多个以','进行分隔", example = "")
    private String supportOsSystem;

    @ApiModelProperty(value = "算法所属大类:1-统计分析,2-特征工程,3-机器学习", example = "")
    private Byte algorithmType;

    @ApiModelProperty(value = "所需的内存 (单位: byte)", example = "")
    private Long costMem;

    @ApiModelProperty(value = "所需的核数 (单位: 个)", example = "")
    private Long costProcessor;

    @ApiModelProperty(value = "GPU核数(单位：核)", example = "")
    private Integer costGpu;

    @ApiModelProperty(value = "所需的带宽 (单位: bps)", example = "")
    private Long costBandwidth;

    @ApiModelProperty(value = "所需的运行时长 (单位: ms)", example = "")
    private Long duration;

    @ApiModelProperty(value = "算法代码", example = "")
    private String algorithmCode;


}
