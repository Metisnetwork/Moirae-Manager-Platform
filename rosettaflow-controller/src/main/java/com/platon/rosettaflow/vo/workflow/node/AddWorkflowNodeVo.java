package com.platon.rosettaflow.vo.workflow.node;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 添加作流节点响应对象
 * @author hudenian
 * @date 2021/8/30
 */
@Data
@ApiModel(value = "添加作流节点响应对象")
public class AddWorkflowNodeVo {

    @ApiModelProperty(value = "工作流节点主键ID")
    private Long workflowNodeId;

}
