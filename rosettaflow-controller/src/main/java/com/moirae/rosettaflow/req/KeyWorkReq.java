package com.moirae.rosettaflow.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "关键字查询")
public class KeyWorkReq {

    @ApiModelProperty(value = "搜索关键字. 任务id跳任务详情（精确匹配）、 组织id跳组织详情（精确匹配）")
    private String keyword;
}
