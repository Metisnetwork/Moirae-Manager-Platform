package com.datum.platform.vo.home;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "数据凭证使用明细")
public class DataUsedVo {

    @ApiModelProperty(value = "数据凭证名称")
    private String name;

    @ApiModelProperty(value = "数据凭证使用量")
    private Long used;
}
