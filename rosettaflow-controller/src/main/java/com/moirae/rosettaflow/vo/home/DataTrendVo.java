package com.moirae.rosettaflow.vo.home;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@ApiModel(value = "走势图数据")
public class DataTrendVo {

    @ApiModelProperty(name = "statsTime", value = "统计时间")
    String statsTime;
    @ApiModelProperty(name = "totalValue", value = "累计值")
    Long totalValue;
    @ApiModelProperty(name = "incrementValue", value = "新增值")
    Long incrementValue;

}
