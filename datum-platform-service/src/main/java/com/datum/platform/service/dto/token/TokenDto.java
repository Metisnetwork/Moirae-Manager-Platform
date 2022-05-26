package com.datum.platform.service.dto.token;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * token信息
 * </p>
 *
 * @author chendai
 * @since 2022-03-28
 */
@Data
@ApiModel(value = "ERC20代币信息")
public class TokenDto {

    @ApiModelProperty(value = "合约地址")
    private String address;

    @ApiModelProperty(value = "合约名称")
    private String name;

    @ApiModelProperty(value = "合约符号")
    private String symbol;

    @ApiModelProperty(value = "合约精度")
    private Integer decimal;
}
