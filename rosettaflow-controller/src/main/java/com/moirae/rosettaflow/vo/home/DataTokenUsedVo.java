package com.moirae.rosettaflow.vo.home;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(value = "数据凭证使用明细")
public class DataTokenUsedVo {

    @ApiModelProperty(value = "排行序号")
    private Integer ranking;

    @ApiModelProperty(value = "数据凭证名称")
    private String dataTokenName;

    @ApiModelProperty(value = "数据凭证使用量")
    private Long dataTokenUsed;
}
