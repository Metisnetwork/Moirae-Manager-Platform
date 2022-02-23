package com.moirae.rosettaflow.vo.home;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "全网统计数据")
public class GlobalStatsVo {

    @ApiModelProperty(name = "totalOrgCount", value = "全网组织数量")
    private int totalOrgCount;

    @ApiModelProperty(name = "powerOrgCount", value = "全是提供算力的组织数量")
    private int powerOrgCount;

    @ApiModelProperty(name = "dataFileSize", value = "全网目前的元数据量，单位：字节")
    private long dataFileSize;

    @ApiModelProperty(name = "usedDataFileSize", value = "全网元数据累计使用量，单位：字节")
    private long usedDataFileSize;

    @ApiModelProperty(name = "taskCount", value = "全网任务数")
    private int taskCount;

    @ApiModelProperty(name = "partnerCount", value = "全网参与过任务的组织数，包括角色是：发起人， 算法提供方，算力提供者，数据提供者，结果消费者")
    private int partnerCount;

    @ApiModelProperty(name = "totalCore", value = "全网目前的算力核心数，单位：个")
    private int totalCore;

    @ApiModelProperty(name = "totalMemory", value = "全网目前的算力内存数，单位：字节")
    private long totalMemory;

    @ApiModelProperty(name = "totalBandwidth", value = "全网目前的带宽，单位：字节")
    private long totalBandwidth;
}
