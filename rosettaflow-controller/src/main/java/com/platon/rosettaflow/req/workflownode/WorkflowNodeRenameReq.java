package com.platon.rosettaflow.req.workflownode;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * 重命名工作流节点名称请求对象
 * @author hudenian
 * @date 2021/8/27
 */
@Data
@ApiModel(value = "重命名工作流节点名称请求对象")
public class WorkflowNodeRenameReq {

    @ApiModelProperty(value = "工作流节点ID", required = true)
    @NotNull(message = "{workflow.node.id.notNull}")
    @Positive(message = "{workflow.node.id.positive}")
    private Long nodeId;

    @ApiModelProperty(value = "工作流节点名称",required = true)
    @NotBlank(message = "{workflow.node.name.NotBlank}")
    private String nodeName;

}
