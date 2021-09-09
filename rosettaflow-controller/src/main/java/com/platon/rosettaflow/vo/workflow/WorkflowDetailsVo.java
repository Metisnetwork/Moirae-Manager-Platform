package com.platon.rosettaflow.vo.workflow;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 工作流详情响应参数
 * @author hudenian
 * @date 2021/8/30
 */
@Data
@ApiModel(value = "工作流详情响应参数")
public class WorkflowDetailsVo {

    @ApiModelProperty(value = "工作流ID")
    private Long id;

    @ApiModelProperty(value = "工作流名称")
    private String workflowName;

    @ApiModelProperty(value = "工作流描述")
    private String workflowDesc;

    @ApiModelProperty(value = "工作流节点数")
    private Integer nodeNumber;

    @ApiModelProperty(value = "运行状态:0-未完成,1-已完成")
    private Integer runStatus;

}
