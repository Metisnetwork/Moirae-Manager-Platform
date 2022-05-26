package com.datum.platform.vo.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "组织统计")
public class OrgStatsVo {

    @ApiModelProperty(value = "组织的总数(可用的)")
    private int orgCount;
}
