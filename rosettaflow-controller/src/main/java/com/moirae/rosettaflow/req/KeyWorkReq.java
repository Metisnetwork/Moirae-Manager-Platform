package com.moirae.rosettaflow.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@ApiModel(value = "关键字查询")
public class KeyWorkReq {

    @ApiModelProperty(value = "搜索关键字")
    private String keyword;
}
