package com.platon.rosettaflow.req.workflow.node;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * 输入请求对象
 * @author hudenian
 * @date 2021/8/31
 */
@Data
@ApiModel(value = "输入请求对象")
public class SaveNodeInputReq {

    @ApiModelProperty(value = "工作流节点ID", required = true)
    @NotNull(message = "{workflow.node.id.notNull}")
    @Positive(message = "{workflow.node.id.positive}")
    private Long workflowNodeId;

    @ApiModelProperty(value = "数据类型(1：结构化数据，2：非结构化数据)(默认传1)", required = true)
    @NotNull(message = "{node.data.type.notNull}")
    private Integer dataType;

    @ApiModelProperty(value = "是否发起方: 0-否, 1-是", required = true)
    @NotNull(message = "{node.sender.flag.notNull}")
    private Integer senderFlag;

    @ApiModelProperty(value = "组织的身份标识Id", required = true)
    @NotBlank(message = "{node.identity.id.NotBlank}")
    private String identityId;

    @ApiModelProperty(value = "数据表ID")
    private String dataTableId;

    @ApiModelProperty(value = "数据字段ID,多个以‘,’分隔")
    @NotBlank(message = "{node.identity.name.NotBlank}")
    private String dataColumnIds;

}
