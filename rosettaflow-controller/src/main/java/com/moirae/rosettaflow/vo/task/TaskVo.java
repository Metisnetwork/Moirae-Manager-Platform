package com.moirae.rosettaflow.vo.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
@ApiModel(value = "任务")
public class TaskVo {

    @ApiModelProperty(value = "任务ID,hash")
    private String id;

    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @ApiModelProperty(value = "任务开始执行时间，精确到毫秒")
    private Date startAt;

    @ApiModelProperty(value = "任务结束时间，精确到毫秒")
    private Date endAt;

    @ApiModelProperty(value = "组织在任务中角色-任务发起方，当这些值>=1时表示是该角色")
    private Integer taskSponsor;

    @ApiModelProperty(value = "组织在任务中角色-算法提供方，当这些值>=1时表示是该角色")
    private Integer algoProvider;

    @ApiModelProperty(value = "组织在任务中角色-结果接收方，当这些值>=1时表示是该角色")
    private Integer resultConsumer;

    @ApiModelProperty(value = "组织在任务中角色-数据提供方，当这些值>=1时表示是该角色")
    private Integer dataProvider;

    @ApiModelProperty(value = "组织在任务中角色-算力提供方，当这些值>=1时表示是该角色")
    private Integer powerProvider;
}
