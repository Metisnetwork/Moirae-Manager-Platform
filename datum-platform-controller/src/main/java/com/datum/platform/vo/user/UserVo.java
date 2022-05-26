package com.datum.platform.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("登录返回参数")
public class UserVo {

    @ApiModelProperty("用户地址")
    private String address;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("用户登录token")
    private String token;
}
