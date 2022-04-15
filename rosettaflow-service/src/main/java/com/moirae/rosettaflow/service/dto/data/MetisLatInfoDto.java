package com.moirae.rosettaflow.service.dto.data;

import com.moirae.rosettaflow.service.dto.token.TokenDto;
import com.moirae.rosettaflow.service.dto.token.TokenHolderDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "MetisLat账户信息")
public class MetisLatInfoDto {

    @ApiModelProperty(value = "数据凭证余额")
    private TokenDto token;

    @ApiModelProperty(value = "已授权给支付合约的金额")
    private TokenHolderDto tokenHolder;
}
