package com.moirae.rosettaflow.service.dto.user;

import com.moirae.rosettaflow.common.utils.AddressChangeUtils;
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
