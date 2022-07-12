package com.datum.platform.vo.home;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


@Data
@ApiModel(value = "走势图数据")
public class TrendVo {

    @ApiModelProperty(value = "统计时间")
    Date statsTime;

    @ApiModelProperty(value = "统计值（隐私 + 非隐私）")
    Long statsValue;

    @ApiModelProperty(value = "统计值（隐私）")
    Long privacyStatsValue;

    @ApiModelProperty(value = "统计值（非隐私）")
    Long noPrivacyStatsValue;
}
