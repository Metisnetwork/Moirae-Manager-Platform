package com.datum.platform.vo.publicity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "公示统计")
public class PublicityStatsVo {

    @ApiModelProperty(value = "组织认证VC数量")
    private int orgVcCount;
}
