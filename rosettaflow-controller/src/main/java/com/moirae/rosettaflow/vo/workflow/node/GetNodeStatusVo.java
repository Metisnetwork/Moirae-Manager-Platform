package com.moirae.rosettaflow.vo.workflow.node;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 工作流节点运行状态对象
 * @author houz
 */
@Data
@ApiModel(value = "工作流节点运行状态对象")
public class GetNodeStatusVo {

    @ApiModelProperty(value = "工作流节点表主键ID")
    private Long workflowNodeId;

    @ApiModelProperty(value = "工作流节点运行状态:0-未开始,1-运行中,2-运行成功,3-运行失败")
    private Byte runStatus;
}
