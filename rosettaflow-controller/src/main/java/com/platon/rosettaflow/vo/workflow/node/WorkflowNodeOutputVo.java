package com.platon.rosettaflow.vo.workflow.node;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 工作流节点输出响应参数
 * @author hudenian
 * @date 2021/8/30
 */
@Data
@ApiModel(value = "工作流节点输出响应参数")
public class WorkflowNodeOutputVo {

    @ApiModelProperty(value = "工作流节点输出表主键ID(自增长)")
    private Long id;

    @ApiModelProperty(value = "工作流节点表主键id")
    private Long workflowNodeId;

    @ApiModelProperty(value = "协同方组织的身份标识Id")
    private String identityId;

    @ApiModelProperty(value = "协同方组织名称")
    private String identityName;

    @ApiModelProperty(value = "存储形式: 1-明文，2:密文")
    private Byte storePattern;

    @ApiModelProperty(value = "存储路径")
    private String storePath;
}
