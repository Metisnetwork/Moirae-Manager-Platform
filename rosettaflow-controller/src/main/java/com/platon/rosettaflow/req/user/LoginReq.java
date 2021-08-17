package com.platon.rosettaflow.req.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author admin
 * @date 2021/8/17
 */
@Data
@ApiModel
public class LoginReq {

    @ApiModelProperty(value = "用户钱包地址", required = true, name = "address", example = "501eb3eeb2a40e6f2ff6f481302435e6e8af3666")
    @NotBlank(message = "{user.address.notBlank}")
//    @Pattern(regexp = "^(lat|lax)\\w{36,42}$", message = "{user.address.format}")
    private String address;

    @ApiModelProperty(value = "钱包地址签名", required = true, name = "sign", example = "HPXfBL0ZYeSMt6GcG8h8zOlPtlA8+LIQvF1AhEq4YZQLNfsgujDFDCzCSr/4ayfw4USAffxxA9OL0xMCVgE5Eg4=")
    @NotBlank(message = "{user.sign.notBlank}")
    private String sign;

}
