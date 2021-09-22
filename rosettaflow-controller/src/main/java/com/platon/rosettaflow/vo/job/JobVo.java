package com.platon.rosettaflow.vo.job;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.platon.rosettaflow.common.constants.SysConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author juzix
 */
@Data
@ApiModel("作业列表返回参数")
public class JobVo {

    @ApiModelProperty("作业id")
    private Long id;

    @ApiModelProperty("作业名称")
    private String name;

    @ApiModelProperty("工作流id")
    private Long workflowId;

    @ApiModelProperty("工作流名称")
    private String workflowName;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = SysConstant.DEFAULT_TIME_PATTERN, timezone = SysConstant.DEFAULT_TIMEZONE)
    private Date createTime;

    @ApiModelProperty("状态: 0-未开始，1-运行中，2-已停止，3-已结束")
    private Byte jobStatus;

    @ApiModelProperty("有效状态: 0-无效，1- 有效")
    private Byte status;


}
