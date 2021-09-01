package com.platon.rosettaflow.vo.workflow;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 工作流节点响应对象
 * @author hudenian
 * @date 2021/8/30
 */
@Data
@ApiModel(value = "工作流节点响应对象")
public class WorkflowNodeVo {

    @ApiModelProperty(value = "工作流节点主键ID")
    private Long id;

    @ApiModelProperty(value = "工作流表主键id")
    private Long workflowId;

    @ApiModelProperty(value = "工作流节点名称")
    private String nodeName;

    @ApiModelProperty(value = "算法id")
    private Long algorithmId;

    @ApiModelProperty(value = "节点在工作流中序号,从1开始")
    private Integer nodeStep;

    @ApiModelProperty(value = "下个节点在工作流中序号,从1开始")
    private Integer nextNodeStep;

    @ApiModelProperty(value = "运行状态:0-未开始,1-运行中,2-运行成功,3-运行失败")
    private Byte runStatus;

    @ApiModelProperty(value = "任务ID,底层处理完成后返回")
    private String taskId;

    @ApiModelProperty(value = "任务处理结果描述")
    private String runMsg;

    @ApiModelProperty(value = "状态: 0-无效，1- 有效")
    private Byte status;

    @ApiModelProperty(value = "算法对象")
    private WorkflowNodeCodeVo workflowNodeCodeVo;

    @ApiModelProperty(value = "环境")
    private WorkflowNodeResourceVo workflowNodeResourceVo;

    @ApiModelProperty(value = "工作流节点输入列表")
    private List<WorkflowNodeInputVo> workflowNodeInputVoList;

    @ApiModelProperty(value = "工作流节点输出列表")
    private List<WorkflowNodeOutputVo> workflowNodeOutputVoList;

}
