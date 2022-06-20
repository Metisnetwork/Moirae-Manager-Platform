package com.datum.platform.vo.data;

import com.datum.platform.vo.task.BaseOrgVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "用户相关的元数据信息")
public class UserAuthDataVo extends DataVo {

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

    @ApiModelProperty(value = "已授权给支付合约的金额")
    private String authorizeBalance;
}
