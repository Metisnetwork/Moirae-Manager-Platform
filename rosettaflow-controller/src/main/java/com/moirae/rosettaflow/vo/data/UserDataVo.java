package com.moirae.rosettaflow.vo.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "用户相关的元数据信息")
public class UserDataVo extends DataVo {

    @ApiModelProperty(value = "数据凭证余额")
    private String tokenBalance;

    @ApiModelProperty(value = "已授权给支付合约的金额")
    private String authorizeBalance;

    @ApiModelProperty(value = "数据凭证精度")
    private Long tokenDecimal;
}
