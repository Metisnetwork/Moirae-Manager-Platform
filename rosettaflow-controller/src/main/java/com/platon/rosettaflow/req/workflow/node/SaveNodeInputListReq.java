package com.platon.rosettaflow.req.workflow.node;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * 添加工作流节点输入请求参数
 * @author hudenian
 * @date 2021/8/31
 */
@Data
@ApiModel(value = "添加工作流节点输入请求参数")
public class SaveNodeInputListReq {

    @ApiModelProperty(value = "工作流节点ID", required = true)
    @NotNull(message = "{workflow.node.id.notNull}")
    @Positive(message = "{workflow.node.id.positive}")
    private Long workflowNodeId;

    @ApiModelProperty(value = "输入请求对象")
    private List<SaveNodeInputReq> saveNodeInputReqList;

}
