package com.datum.platform.service.dto.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "无属性凭证信息")
public class NoAttributeCredentialDto {

    @ApiModelProperty(value = "数据凭证合约地址")
    private String tokenAddress;

    @ApiModelProperty(value = "数据凭证名称")
    private String tokenName;

    @ApiModelProperty(value = "数据凭证符号")
    private String tokenSymbol;

    @ApiModelProperty(value = "数据凭证精度")
    private Long tokenDecimal;

    @ApiModelProperty(value = "数据凭证余额")
    private String tokenBalance;
}
