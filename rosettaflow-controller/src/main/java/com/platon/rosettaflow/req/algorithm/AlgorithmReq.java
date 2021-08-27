package com.platon.rosettaflow.req.algorithm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author houz
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "算法列表请求参数")
public class AlgorithmReq {

    @ApiModelProperty(value = "算法ID", example = "1")
    private Long id;

    @ApiModelProperty(value = "算法名称", example = "名称", required = true)
    @NotBlank(message = "{algorithm.name.notBlank}")
    private String algorithmName;

    @ApiModelProperty(value = "算法描述", example = "描述")
    private String algorithmDesc;

    @ApiModelProperty(value = "支持协同方最大数量", example = "2", required = true)
    @NotBlank(message = "{support.partner.max.number.notBlank}")
    private Long maxNumbers;

    @ApiModelProperty(value = "支持协同方最小数量", example = "3", required = true)
    @NotBlank(message = "{support.partner.min.number.notBlank}")
    private Long minNumbers;

    @ApiModelProperty(value = "支持语言,多个以','进行分隔")
    private String supportLanguage;

    @ApiModelProperty(value = "支持操作系统,多个以','进行分隔")
    private String supportOsSystem;

    @ApiModelProperty(value = "算法所属大类:1-统计分析,2-特征工程,3-机器学习", example = "1")
    @NotBlank(message = "{algorithm.reside.type.notBlank}")
    private Byte algorithmType;

    @ApiModelProperty(value = "所需的内存 (单位: byte)", example = "2000")
    private Long costMem;

    @ApiModelProperty(value = "所需的核数 (单位: 个)", example = "4")
    private Long costProcessor;

    @ApiModelProperty(value = "GPU核数(单位：核)", example = "4")
    private Integer costGpu;

    @ApiModelProperty(value = "所需的带宽 (单位: bps)", example = "200")
    private Long costBandwidth;

    @ApiModelProperty(value = "所需的运行时长 (单位: ms)", example = "20000")
    private Long duration;

    @ApiModelProperty(value = "算法代码", example = "code")
    private String algorithmCode;


}
