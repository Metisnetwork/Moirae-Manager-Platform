package com.moirae.rosettaflow.service.dto.workflow.expert;

import com.moirae.rosettaflow.service.dto.workflow.common.OutputDto;
import com.moirae.rosettaflow.service.dto.workflow.common.ResourceDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * 专家模式节点的代码明细
 */
@Data
@ApiModel(value = "专家模式节点明细")
public class NodeDto {

    @ApiModelProperty(value = "工作流节点序号,从1开始", required = true)
    @NotNull(message = "{workflow.node.step.notNull}")
    @Positive(message = "{workflow.node.step.positive}")
    private Integer nodeStep;

    @ApiModelProperty(value = "工作流节点名称", required = true)
    @NotBlank(message = "{workflow.node.name.NotBlank}")
    private String nodeName;

    @ApiModelProperty(value = "工作流节点算法", required = true)
    @NotNull(message = "{algorithm.id.notNull}")
    @Positive(message = "{algorithm.id.positive}")
    private Long algorithmId;

    @ApiModelProperty(value = "工作流节点输入", required = true)
    private NodeInputDto nodeInput;

    @ApiModelProperty(value = "工作流节点输出", required = true)
    private OutputDto nodeOutput;

    @ApiModelProperty(value = "工作流节点代码", required = true)
    private NodeCodeDto nodeCode;

    @ApiModelProperty(value = "工作流节点环境", required = true)
    private ResourceDto resource;
}
