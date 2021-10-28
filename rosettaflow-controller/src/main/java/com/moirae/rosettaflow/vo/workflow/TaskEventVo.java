package com.moirae.rosettaflow.vo.workflow;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.moirae.rosettaflow.common.constants.SysConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author hudenian
 * @date 2021/8/30
 * @description 获取任务事件响应对象
 */
@Data
@ApiModel
public class TaskEventVo {

    @ApiModelProperty(value = "事件类型码")
    private String type;

    @ApiModelProperty(value = "事件对应的任务id")
    private String taskId;

    @ApiModelProperty(value = "组织名称")
    private String name;

    @ApiModelProperty(value = "组织中调度服务的 nodeId")
    private String nodeId;

    @ApiModelProperty(value = "组织的身份标识Id")
    private String identityId;

    @ApiModelProperty(value = "事件内容")
    private String content;

    @ApiModelProperty(value = "事件产生时间")
    @JsonFormat(pattern = SysConstant.DEFAULT_TIME_PATTERN, timezone = SysConstant.DEFAULT_TIMEZONE)
    private Date createAt;

}
