package com.platon.rosettaflow.req.workflownode;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * @author hudenian
 * @date 2021/8/27
 * @description 修改工作流节点名称请求对象
 */
@Data
@ApiModel
public class WorkflowNodeRenameReq {

    @ApiModelProperty(value = "工作流ID", required = true)
    @NotNull(message = "{workflow.id.notNull}")
    @Positive(message = "{workflow.id.positive}")
    private Long workflowId;

    @ApiModelProperty(value = "工作流节点名称",required = true)
    @NotBlank(message = "工作流节点名称不能为空")
    private String nodeName;

    @ApiModelProperty(value = " 节点在工作流中序号,从1开始", required = true)
    @NotNull(message = " 节点在工作流中序号不能为空")
    @Positive(message = " 节点在工作流中序号错误")
    private Integer nodeStep;

}
