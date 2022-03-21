package com.moirae.rosettaflow.vo.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "数据统计")
public class DataStatsVo {

    @ApiModelProperty(value = "数据的总数(可用的)")
    private int dataCount;
}
