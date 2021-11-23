package com.moirae.rosettaflow.vo.algorithm;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author houz
 */
@Data
@ApiModel("算法列表响应参数")
public class AlgorithmListVo {

    @ApiModelProperty(value = "算法ID")
    private Long algorithmId;

    @ApiModelProperty(value = "算法名称")
    private String algorithmName;

    @ApiModelProperty(value = "算法描述")
    private String algorithmDesc;

    @ApiModelProperty(value = "算法类型：1-统计分析,2-特征工程,3-机器学习")
    private Byte algorithmType;

    @ApiModelProperty(value = "作者")
    private String author;

    @ApiModelProperty(value = "支持协同方最小数量")
    private Long minNumbers;

    @ApiModelProperty(value = "支持语言,多个以\",\"进行分隔")
    private String supportLanguage;

    @ApiModelProperty(value = "算法代码")
    private String calculateContractCode;



}
