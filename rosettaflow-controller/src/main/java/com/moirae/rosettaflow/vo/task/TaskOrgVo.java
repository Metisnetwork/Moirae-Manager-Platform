package com.moirae.rosettaflow.vo.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "组织关联的任务")
public class TaskOrgVo extends BaseTaskVo {

    @ApiModelProperty(value = "该组织在任务中角色-是否任务发方")
    private Boolean isTaskSponsor;

    @ApiModelProperty(value = "该组织在任务中角色-是否算法提供方")
    private Boolean isAlgoProvider;

    @ApiModelProperty(value = "该组织在任务中角色-是否数据提供方")
    private Boolean isDataProvider;

    @ApiModelProperty(value = "该组织在任务中角色-是否算力提供方")
    private Boolean isPowerProvider;

    @ApiModelProperty(value = "该组织在任务中角色-是否结果接收方")
    private Boolean isResultReceiver;
}
