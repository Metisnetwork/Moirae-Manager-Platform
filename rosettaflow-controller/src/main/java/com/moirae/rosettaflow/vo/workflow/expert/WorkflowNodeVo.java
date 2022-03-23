package com.moirae.rosettaflow.vo.workflow.expert;

import com.moirae.rosettaflow.vo.model.ModelVo;
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

    @ApiModelProperty(value = "节点模型ID")
    private ModelVo model;

    @ApiModelProperty(value = "任务ID,底层处理完成后返回")
    private String taskId;

    @ApiModelProperty(value = "任务处理结果描述")
    private String runMsg;

    @ApiModelProperty(value = "任务发启放组织id")
    private String workflowNodeSenderIdentityId;

    @ApiModelProperty(value = "算法对象")
    private NodeAlgorithmVo nodeAlgorithmVo;

    @ApiModelProperty(value = "工作流节点输入列表")
    private List<WorkflowNodeInputVo> workflowNodeInputVoList;

    @ApiModelProperty(value = "工作流节点输出列表")
    private List<WorkflowNodeOutputVo> workflowNodeOutputVoList;

}
