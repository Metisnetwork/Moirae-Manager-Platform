package com.datum.platform.service.dto.workflow;

import com.datum.platform.service.dto.task.TaskEventDto;
import com.datum.platform.service.dto.task.TaskResultDto;
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

    @ApiModelProperty(value = "任务结果文件")
    private List<TaskResultDto> taskResultList;

    @ApiModelProperty(value = "任务事件")
    private List<TaskEventDto> eventList;

    @ApiModelProperty(value = "模型评估结果")
    private String modelEvaluate;

    @ApiModelProperty(value = "信息价值评估指标")
    private String informationValueEvaluate;
}
