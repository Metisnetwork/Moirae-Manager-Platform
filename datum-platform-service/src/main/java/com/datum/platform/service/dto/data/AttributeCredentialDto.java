package com.datum.platform.service.dto.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "有属性凭证信息")
public class AttributeCredentialDto {

    @ApiModelProperty(value = "数据凭证合约地址")
    private String tokenAddress;

    @ApiModelProperty(value = "数据凭证名称")
    private String tokenName;

    @ApiModelProperty(value = "数据凭证符号")
    private String tokenSymbol;

    @ApiModelProperty(value = "tokenId")
    private String tokenId;

    @ApiModelProperty(value = "token id 对应特性值，如有效期")
    private String characteristic;
}
