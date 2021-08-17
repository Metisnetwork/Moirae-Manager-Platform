package com.platon.rosettaflow.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hudenian
 * @date 2021/8/16
 * @description 用户信息返回对象
 */
@Data
@ApiModel
public class UserVo {

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("用户钱包地址")
    private String address;

    @ApiModelProperty("用户状态: 0-无效，1- 有效")
    private Byte status;

    @ApiModelProperty("用户登录token")
    private String token;
}
