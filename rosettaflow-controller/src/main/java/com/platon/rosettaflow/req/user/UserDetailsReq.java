package com.platon.rosettaflow.req.user;

import com.platon.rosettaflow.common.utils.AddressChangeUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 查询用户详情请求参数
 * @author houz
 */
@Data
@ApiModel(value = "查询用户详情请求参数")
public class UserDetailsReq {

    @ApiModelProperty(value = "用户钱包地址", required = true, example = "0xb0eea1efd5f215278b420d21c0bf5cd6451aa4c7")
    @NotBlank(message = "{user.address.notBlank}")
    private String address;

    public String getAddress(){
        return AddressChangeUtils.convert0XAddress(address);
    }
}
