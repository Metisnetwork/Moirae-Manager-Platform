package com.datum.platform.service.dto.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "有属性凭证信息")
public class HaveAttributesCredentialDto extends BaseCredentialDto {

    @ApiModelProperty(value = "tokenId")
    private String tokenId;

    @ApiModelProperty(value = "有效期，时间戳")
    private String characteristic;
}
