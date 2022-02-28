package com.moirae.rosettaflow.vo.task;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "组织关联的任务")
public class TaskVo extends BaseTaskVo {
}
