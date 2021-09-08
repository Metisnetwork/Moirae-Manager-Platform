package com.platon.rosettaflow.req.workflow.node;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * 添加工作流节点代码请求对象
 * @author hudenian
 * @date 2021/8/31
 */
@Data
@ApiModel(value = "添加工作流节点代码请求对象")
public class SaveNodeCodeReq {

    @ApiModelProperty(value = "工作流ID", required = true)
    @NotNull(message = "{workflow.node.id.notNull}")
    @Positive(message = "{workflow.node.id.positive}")
    private Long workflowNodeId;

    @ApiModelProperty(value = "算法编辑类型", required = true)
    @NotNull(message = "{workflow.node.algorithm.type.notNull}")
    private Integer editType;

    @ApiModelProperty(value = "算法代码", required = true)
    @NotBlank(message = "{workflow.node.algorithm.code.NotBlank}")
    private String calculateContractCode;

    @ApiModelProperty(value = "算法数据分片")
    private String dataSplitContractCode;

}
