package com.datum.platform.vo.home;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "全网统计数据")
public class StatsGlobalVo {

    @ApiModelProperty(value = "计算总次数(总的任务数,包括成功和失败的)")
    private int taskCount;

    @ApiModelProperty(value = "参与计算地址总数")
    private int addressCountOfTask;

    @ApiModelProperty(value = "24h活跃地址总数")
    private int addressCountOfActive;

    @ApiModelProperty(value = "全网数据总量，单位：字节")
    private long dataSize;

    @ApiModelProperty(value = "全网数据个数")
    private int dataCount;

    @ApiModelProperty(value = "数据使用总次数")
    private long dataUsed;

    @ApiModelProperty(value = "全网目前的算力核心数，单位：个")
    private int totalCore;

    @ApiModelProperty(value = "全网目前的算力内存数，单位：字节")
    private long totalMemory;

    @ApiModelProperty(value = "全网目前的带宽，单位：字节")
    private long totalBandwidth;
}
