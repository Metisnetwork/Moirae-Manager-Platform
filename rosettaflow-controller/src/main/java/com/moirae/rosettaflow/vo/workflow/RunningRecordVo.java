package com.moirae.rosettaflow.vo.workflow;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel
public class RunningRecordVo {

    @ApiModelProperty(value = "工作流运行记录id")
    private Long id;

    @ApiModelProperty(value = "工作流id")
    private Long workflowId;

    @ApiModelProperty(value = "工作流名称")
    private String workflowName;

    @ApiModelProperty(value = "工作流编辑的版本号，可以定位到当时执行时配置信息")
    private Integer workflowEditVersion;

    @ApiModelProperty(value = "总步骤数")
    private Integer step;

    @ApiModelProperty(value = "当前步骤数")
    private Integer curStep;

    @ApiModelProperty(value = "开始时间")
    private Date beginTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "运行状态: 1-运行中,2-运行成功,3-运行失败")
    private Byte runStatus;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
