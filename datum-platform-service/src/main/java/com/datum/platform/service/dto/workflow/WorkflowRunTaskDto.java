package com.datum.platform.service.dto.workflow;

import com.datum.platform.common.constants.SysConstant;
import com.datum.platform.common.utils.LanguageContext;
import com.datum.platform.mapper.enums.WorkflowTaskRunStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Objects;

@Data
@ApiModel(value = "工作流运行任务")
public class WorkflowRunTaskDto {

    @ApiModelProperty(value = "运行任务ID")
    private Long id;

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

    @ApiModelProperty(value = "任务执行状态  0-未开始 1-运行中,2-运行成功,3-运行失败")
    private WorkflowTaskRunStatusEnum status;

    @ApiModelProperty(value = "任务发起时间，精确到毫秒(flow的发起)")
    private Date createTime;

    @ApiModelProperty(value = "任务ID")
    private String taskId;

    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @ApiModelProperty(value = "工作流版本名称")
    private String workflowVersionName;

    @ApiModelProperty(value = "是否产生模型: 0-否，1:是")
    private Boolean outputModel;
}
