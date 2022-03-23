package com.moirae.rosettaflow.req.workflow.expert;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * 保存工作流节点请求对象
 *
 * @author hudenian
 * @date 2021/9/28
 */
@Data
@ApiModel(value = "工作流详情请求对象")
public class WorkflowNodeReq {

    @ApiModelProperty(value = "发起方的组织的身份标识Id", required = true)
    @NotBlank(message = "{workflow.node.sender.NotBlank}")
    private String identityId;

    @ApiModelProperty(value = "算法ID", required = true)
    @NotNull(message = "{algorithm.id.notNull}")
    @Positive(message = "{algorithm.id.positive}")
    private Long algorithmId;

    @ApiModelProperty(value = "工作流节点名称", required = true)
    @NotBlank(message = "{workflow.node.name.NotBlank}")
    private String nodeName;

    @ApiModelProperty(value = "工作流当前节点序号,从1开始", required = true)
    @NotNull(message = "{workflow.node.step.notNull}")
    @Positive(message = "{workflow.node.step.positive}")
    private Integer nodeStep;

    @ApiModelProperty(value = "工作流当前节点模型ID")
    private Long modelId;

    @ApiModelProperty(value = "是否需要输入模型: 0-否，1:是")
    private Integer inputModel;

    @ApiModelProperty(value = "输入请求列表")
    private List<WorkflowNodeInputReq> workflowNodeInputReqList;

    @ApiModelProperty(value = "输出请求列表")
    private List<WorkflowNodeOutputReq> workflowNodeOutputReqList;

    @ApiModelProperty(value = "工作流节点代码请求对象")
    private WorkflowNodeCodeReq workflowNodeCodeReq;

    @ApiModelProperty(value = "工作流节点资源请求对象")
    private WorkflowNodeResourceReq workflowNodeResourceReq;

    @ApiModelProperty(value = "工作流节点输入变量请求对象")
    private List<WorkflowNodeVariableReq> workflowNodeVariableReqList;

}
