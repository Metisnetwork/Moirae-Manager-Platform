package com.platon.rosettaflow.req.workflow;

import com.platon.rosettaflow.req.workflow.node.WorkflowAllNodeReq;
import com.platon.rosettaflow.req.workflow.node.WorkflowNodeReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/17
 * @description 启动工作流请求实例
 */
@Data
@ApiModel
public class StartWorkflowReq {

    @ApiModelProperty(value = "工作流ID", required = true)
    @NotNull(message = "{workflow.id.notNull}")
    @Positive(message = "{workflow.id.positive}")
    private Long workflowId;

    @ApiModelProperty(value = "起始节点", required = true)
    @NotNull(message = "{workflow.startNode.notNull}")
    @Positive(message = "{workflow.startNode.positive}")
    private Integer startNode;

    @ApiModelProperty(value = "截止节点", required = true)
    @NotNull(message = "{workflow.endNode.notNull}")
    @Positive(message = "{workflow.endNode.positive}")
    private Integer endNode;

    @ApiModelProperty(value = "用户钱包地址", required = true)
    @NotBlank(message = "{user.address.notBlank}")
    private String address;

    @ApiModelProperty(value = "发起任务的账户的签名", required = true)
    @NotBlank(message = "{user.sign.notBlank}")
    private String sign;

    @ApiModelProperty(value = "是否需要保存（0：不需要，1：需要）", required = true)
    @NotBlank(message = "{node.save.flag.NotBlank}")
    private String saveFlag;

    @ApiModelProperty(value = "工作流所有节点列表")
    List<WorkflowNodeReq> workflowNodeReqList;

}
