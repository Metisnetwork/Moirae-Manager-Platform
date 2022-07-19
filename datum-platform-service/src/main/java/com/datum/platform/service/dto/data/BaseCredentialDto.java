package com.datum.platform.service.dto.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class BaseCredentialDto {

    @ApiModelProperty(value = "凭证id")
    private Long id;

    @ApiModelProperty(value = "凭证类型: 0-无属性,1-有属性")
    private Integer type;

    @ApiModelProperty(value = "元数据id")
    private String metaDataId;

    @ApiModelProperty(value = "凭证对应合约地址")
    private String tokenAddress;

    @ApiModelProperty(value = "凭证名称")
    private String tokenName;

    @ApiModelProperty(value = "是否支持明文算法")
    private Boolean isSupportPtAlg;

    @ApiModelProperty(value = "是否支持密文算法")
    private Boolean isSupportCtAlg;
}
