package com.platon.rosettaflow.req.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author admin
 * @date 2021/8/17
 */
@Data
@ApiModel("登录请求参数")
public class LoginInReq {

    @ApiModelProperty(value = "用户钱包地址", required = true, notes = "address", example = "501eb3eeb2a40e6f2ff6f481302435e6e8af3666")
    @NotBlank(message = "{user.address.notBlank}")
//    @Pattern(regexp = "^(lat|lax)\\w{36,42}$", message = "{user.address.format}")
    private String address;

    @ApiModelProperty(value = "用户类型 0: 未定义; 1: 以太坊地址; 2: Alaya地址; 3: PlatON地址", required = true)
    @NotNull(message = "{user.type.noBlank}")
    @Range(min = 0, max = 3, message = "{user.type.range.error}")
    private Byte userType;

    @ApiModelProperty(value = "签名明文(json格式字符串)", required = true,example = "{\"domain\":{\"name\":\"Moirae\"},\"message\":{\"key\":\"uuid\",\"desc\":\"Login to Moirae\"},\"primaryType\":\"Login\",\"types\":{\"EIP712Domain\":[{\"name\":\"name\",\"type\":\"string\"}],\"Login\":[{\"name\":\"key\",\"type\":\"string\"},{\"name\":\"desc\",\"type\":\"string\"}]}}")
    @NotNull(message = "{user.sign.plaintext.notBlank}")
    private String signMessage;

    @ApiModelProperty(value = "签名", required = true, notes = "sign", example = "HPXfBL0ZYeSMt6GcG8h8zOlPtlA8+LIQvF1AhEq4YZQLNfsgujDFDCzCSr/4ayfw4USAffxxA9OL0xMCVgE5Eg4=")
    @NotBlank(message = "{user.sign.notBlank}")
    private String sign;



}
