package com.datum.platform.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class IdReq {

    @ApiModelProperty(value = "id")
    private String id;
}
