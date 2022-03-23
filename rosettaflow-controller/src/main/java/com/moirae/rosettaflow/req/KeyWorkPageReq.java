package com.moirae.rosettaflow.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "元数据列表字段请求参数")
public class KeyWorkPageReq extends CommonPageReq {

    @ApiModelProperty(value = "搜索关键字")
    private String keyword;
}
