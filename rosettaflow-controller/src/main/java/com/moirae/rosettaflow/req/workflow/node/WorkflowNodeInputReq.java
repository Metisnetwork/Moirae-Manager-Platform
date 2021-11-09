package com.moirae.rosettaflow.req.workflow.node;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 输入请求对象
 * @author hudenian
 * @date 2021/9/28
 */
@Data
@ApiModel(value = "工作流节点输入请求对象")
public class WorkflowNodeInputReq {

    @ApiModelProperty(value = "组织的身份标识Id", required = true)
    @NotBlank(message = "{node.identity.id.NotBlank}")
    private String identityId;

    @ApiModelProperty(value = "用户数据授权表主键ID")
    private Long dataTableId;

    @ApiModelProperty(value = "ID列(列索引)")
    @NotNull(message = "{node.identity.name.id.column}")
    private Long keyColumn;

    @ApiModelProperty(value = "因变量（标签）")
    @NotNull(message = "{node.identity.dependent.variable.column}")
    private Long dependentVariable;

    @ApiModelProperty(value = "数据字段ID,多个以‘,’分隔")
    @NotBlank(message = "{node.identity.name.NotBlank}")
    private String dataColumnIds;

    @ApiModelProperty(value = "是否发起方: 0-否, 1-是", required = true)
    @NotNull(message = "{node.sender.flag.notNull}")
    private Integer senderFlag;

}
