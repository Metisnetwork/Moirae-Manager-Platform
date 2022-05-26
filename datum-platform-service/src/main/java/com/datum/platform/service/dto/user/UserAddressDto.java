package com.datum.platform.service.dto.user;

import com.datum.platform.common.utils.AddressChangeUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "用户地址")
public class UserAddressDto {

    @ApiModelProperty(value = "用户地址")
    private String address;

    public String getAddress(){
        return AddressChangeUtils.convert0xAddress(address);
    }
}
