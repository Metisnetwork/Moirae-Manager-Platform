package com.moirae.rosettaflow.vo.workflow;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "工作流列表响应对象")
public class WorkflowVersionVo {

    @ApiModelProperty(value = "工作流ID")
    private Long id;

    @ApiModelProperty(value = "工作流版本号")
    private Integer version;

    @ApiModelProperty(value = "工作流版本名称")
    private String workflowVersionName;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "工作流版本执行状态（未支付、继续支付、已支付、运行中、运行成功、运行失败）")
    private String status;

    @ApiModelProperty(value = "开始时间")
    private Date beginTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;
}
