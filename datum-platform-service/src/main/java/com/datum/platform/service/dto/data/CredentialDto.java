package com.datum.platform.service.dto.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class CredentialDto extends CredentialKeyDto {

    @ApiModelProperty(value = "数据凭证名称")
    private String tokenName;

    @ApiModelProperty(value = "数据凭证符号")
    private String tokenSymbol;

    @ApiModelProperty(value = "数据凭证精度")
    private Long tokenDecimal;

    @ApiModelProperty(value = "数据凭证余额")
    private String tokenBalance;

    @ApiModelProperty(value = "tokenId 对应特性值，如有效期，时间戳")
    private String characteristic;
}
