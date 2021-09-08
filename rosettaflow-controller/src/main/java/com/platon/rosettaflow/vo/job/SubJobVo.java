package com.platon.rosettaflow.vo.job;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author juzix
 */
@Data
@ApiModel("子作业列表返回参数")
public class SubJobVo {

    @ApiModelProperty("子作业id")
    private Long id;

    @ApiModelProperty("作业状态:0-未开始,1-运行中,2-运行成功,3-运行失败")
    private Byte subJobStatus;

    @ApiModelProperty("开始时间")
    private Date beginTime;

    @ApiModelProperty("结束时间")
    private Date endTime;

    @ApiModelProperty("运行时长")
    private String runTime;

    @ApiModelProperty("工作流id")
    private Long workflowId;

}
