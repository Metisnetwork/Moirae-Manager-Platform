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
public class TokenHolderDto {

    @ApiModelProperty(value = "用户地址")
    private String address;

    @ApiModelProperty(value = "地址代币余额, ERC20为数量")
    private String balance;

    @ApiModelProperty(value = "地址已授权支付助手的代币余额, ERC20为数量")
    private String authorizeBalance;
}
