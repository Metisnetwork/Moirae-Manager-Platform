package com.moirae.rosettaflow.vo.workflow.node;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 工作流节点输入响应参数
 * @author hudenian
 * @date 2021/8/30
 */
@Data
@ApiModel(value = "工作流节点输入响应参数")
public class WorkflowNodeInputVo {

    @ApiModelProperty(value = "工作流节点输入表主键ID")
    private Long id;

    @ApiModelProperty(value = "工作流节点id")
    private Long workflowNodeId;

    @ApiModelProperty(value = "组织的身份标识Id")
    private String identityId;

    @ApiModelProperty(value = "元数据id")
    private String dataTableId;

    @ApiModelProperty(value = "数据字段ID,多个以”,“分隔")
    private String dataColumnIds;

    @ApiModelProperty(value = "ID列(列索引)")
    private Long keyColumn;

    @ApiModelProperty(value = "因变量(标签)")
    private Long dependentVariable;
}
