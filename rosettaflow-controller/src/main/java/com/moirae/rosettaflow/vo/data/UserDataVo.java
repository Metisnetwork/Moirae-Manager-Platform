package com.moirae.rosettaflow.vo.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "元数据信息")
public class UserDataVo extends DataVo {

    @ApiModelProperty(value = "数据凭证余额")
    private String tokenBalance;

    @ApiModelProperty(value = "数据凭证精度")
    private Long tokenDecimal;
}
