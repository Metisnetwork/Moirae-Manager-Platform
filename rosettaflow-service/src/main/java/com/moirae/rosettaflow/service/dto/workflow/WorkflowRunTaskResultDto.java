package com.moirae.rosettaflow.service.dto.workflow;

import com.moirae.rosettaflow.mapper.enums.TaskStatusEnum;
import com.moirae.rosettaflow.service.dto.task.TaskEventDto;
import com.moirae.rosettaflow.service.dto.task.TaskResultDto;
import com.moirae.rosettaflow.service.dto.workflow.WorkflowRunTaskDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class WorkflowRunTaskResultDto extends WorkflowRunTaskDto {

    @ApiModelProperty(value = "任务计算结束时间，精确到毫秒")
    private Date endAt;

    @ApiModelProperty(value = "算力提供方")
    private List<TaskResultDto> taskResultList;

    @ApiModelProperty(value = "任务事件")
    private List<TaskEventDto> eventList;

    @ApiModelProperty(value = "模型评估结果")
    private String modelEvaluate;
}
