package com.datum.platform.vo.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "任务统计")
public class TaskStatsVo {

    @ApiModelProperty(value = "隐私计算总次数(总的任务数,包括成功和失败的)")
    private int taskCount;
}
