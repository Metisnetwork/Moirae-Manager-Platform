package com.platon.rosettaflow.vo.job;

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
    private long workflowId;

    @ApiModelProperty("工作流名称")
    private String workflowName;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("状态: 0-未结束，1-已结束")
    private Byte jobStatus;

    @ApiModelProperty("有效状态: 0-无效，1- 有效")
    private Byte status;


}
