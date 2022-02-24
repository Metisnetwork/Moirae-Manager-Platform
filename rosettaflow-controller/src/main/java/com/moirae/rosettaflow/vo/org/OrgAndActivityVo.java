package com.moirae.rosettaflow.vo.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "组织及活跃度信息")
public class OrgAndActivityVo extends BaseOrgVo{

    @ApiModelProperty(value = "过去30天内的任务数")
    private Integer taskCount;

    @ApiModelProperty(value = "空闲的天数")
    private Integer idleDays;
}
