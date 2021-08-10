package com.platon.rosettaflow.req.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author admin
 * @date 2021/7/20
 */
@Data
@ApiModel
public class RegisterReq {

    @ApiModelProperty(value = "用户名", required = true, name = "name", example = "hudenian")
    @NotBlank(message = "{user.name.notBlank}")
    private String name;

    @ApiModelProperty(value = "密码", required = true, name = "password", example = "123456")
    @NotBlank(message = "{user.password.notBlank}")
    private String password;

}
