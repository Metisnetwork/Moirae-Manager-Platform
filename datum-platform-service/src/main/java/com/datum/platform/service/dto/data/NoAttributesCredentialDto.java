package com.datum.platform.service.dto.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "无属性凭证信息")
public class NoAttributesCredentialDto extends BaseCredentialDto {

    @ApiModelProperty(value = "数据凭证符号")
    private String tokenSymbol;

    @ApiModelProperty(value = "数据凭证精度")
    private Long tokenDecimal;

    @ApiModelProperty(value = "无属性凭证明文算法消耗量")
    private String erc20PtAlgConsume;

    @ApiModelProperty(value = "无属性凭证密文算法消耗量")
    private String erc20CtAlgConsume;
}
