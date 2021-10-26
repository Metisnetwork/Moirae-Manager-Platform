package com.platon.rosettaflow.req.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author admin
 * @date 2021/7/20
 */
@Data
@ApiModel
public class GrpcReq {

    @ApiModelProperty(value = "firstName", required = true, name = "firstName", example = "hu")
    private String firstName;

    @ApiModelProperty(value = "lastName", required = true, name = "lastName", example = "nian")
    private String lastName;

}
