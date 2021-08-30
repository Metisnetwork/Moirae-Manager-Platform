package com.platon.rosettaflow.req.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author admin
 * @date 2021/8/17
 */
@Data
@ApiModel("修改昵称请求参数")
public class UpdateNickReq {

    @ApiModelProperty(value = "用户钱包地址", required = true, example = "501eb3eeb2a40e6f2ff6f481302435e6e8af3666")
    @NotBlank(message = "{user.address.notBlank}")
    private String address;

    @ApiModelProperty(value = "昵称", required = true, example = "用户1")
    @NotBlank(message = "{user.nickname.notBlank}")
    private String nickName;
}
