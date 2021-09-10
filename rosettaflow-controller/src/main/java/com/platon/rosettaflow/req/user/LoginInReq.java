package com.platon.rosettaflow.req.user;

import com.platon.rosettaflow.common.utils.AddressChangeUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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

    @ApiModelProperty(value = "签名明文(json格式字符串)", required = true,example = "{\"domain\":{\"name\":\"Moirae\"},\"message\":{\"key\":\"26e65a54b17e44b896a7f9a0353856d6\",\"desc\":\"Welcome to Moirae!\"},\"primaryType\":\"Login\",\"types\":{\"EIP712Domain\":[{\"name\":\"name\",\"type\":\"string\"}],\"Login\":[{\"name\":\"key\",\"type\":\"string\"},{\"name\":\"desc\",\"type\":\"string\"}]}}")
    @NotNull(message = "{user.sign.plaintext.notBlank}")
    private String signMessage;

    @ApiModelProperty(value = "签名", required = true, notes = "sign", example = "HPXfBL0ZYeSMt6GcG8h8zOlPtlA8+LIQvF1AhEq4YZQLNfsgujDFDCzCSr/4ayfw4USAffxxA9OL0xMCVgE5Eg4=")
    @NotBlank(message = "{user.sign.notBlank}")
    private String sign;

    /**
     * @return : 返回0x地址
     */
    public String getAddress(){
        return AddressChangeUtils.convert0XAddress(address);
    }

    /**
     * @return : 返回hrp地址
     */
    public String getHrpAddress(){
        return address;
    }





}
