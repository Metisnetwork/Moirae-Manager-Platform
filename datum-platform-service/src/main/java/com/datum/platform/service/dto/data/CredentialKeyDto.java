package com.datum.platform.service.dto.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class CredentialKeyDto {

    @ApiModelProperty(value = "元数据id")
    private String metaDataId;

    @ApiModelProperty(value = "凭证类型: 0-无属性,1-有属性")
    private Integer type;

    @ApiModelProperty(value = "数据凭证合约地址")
    private String tokenAddress;

    @ApiModelProperty(value = "凭证类似如果是721,则存在")
    private String tokenId;
}
