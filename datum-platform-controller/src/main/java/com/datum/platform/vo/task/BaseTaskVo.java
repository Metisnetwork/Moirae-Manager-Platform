package com.datum.platform.vo.task;

import com.datum.platform.mapper.enums.TaskStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class BaseTaskVo {

    @ApiModelProperty(value = "任务ID,hash")
    private String id;

    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @ApiModelProperty(value = "任务发起时间，精确到毫秒")
    private Date createAt;

    @ApiModelProperty(value = "任务计算开始时间，精确到毫秒")
    private Date startAt;

    @ApiModelProperty(value = "任务计算结束时间，精确到毫秒")
    private Date endAt;

    @ApiModelProperty(value = "任务执行状态  0:未知; 1:等待中; 2:计算中; 3:失败; 4:成功")
    private TaskStatusEnum status;
}
