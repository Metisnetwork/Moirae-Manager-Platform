package com.moirae.rosettaflow.service.dto.workflow;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.moirae.rosettaflow.common.constants.SysConstant;
import com.moirae.rosettaflow.common.utils.LanguageContext;
import com.moirae.rosettaflow.mapper.enums.TaskStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Objects;

@Data
@ApiModel(value = "工作流运行任务")
public class WorkflowRunTaskDto {

    @ApiModelProperty(value = "运行任务ID")
    private Integer id;

    @JsonIgnore
    private String algorithmName;

    @JsonIgnore
    private String algorithmNameEn;

    @ApiModelProperty(value = "任务类别")
    public String getTaskType(){
        if (Objects.nonNull(LanguageContext.get()) && LanguageContext.get().equals(SysConstant.EN_US)) {
            return algorithmNameEn;
        }
        return algorithmName;
    }

    @ApiModelProperty(value = "任务执行状态  0:未知; 1:等待中; 2:计算中; 3:失败; 4:成功")
    private TaskStatusEnum status;

    @ApiModelProperty(value = "任务发起时间，精确到毫秒(flow的发起)")
    private Date createTime;

    @ApiModelProperty(value = "任务ID")
    private String taskId;
}
