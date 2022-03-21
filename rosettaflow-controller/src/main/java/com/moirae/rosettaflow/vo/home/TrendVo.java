package com.moirae.rosettaflow.vo.home;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@ApiModel(value = "走势图数据")
public class TrendVo {

    @ApiModelProperty(value = "统计时间")
    String statsTime;

    @ApiModelProperty(value = "统计值")
    Long statsValue;
}
