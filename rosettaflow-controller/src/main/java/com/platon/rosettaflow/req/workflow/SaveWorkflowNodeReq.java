package com.platon.rosettaflow.req.workflow;

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
 * @description 修改工作流明细保存请求对象
 */
@Data
@ApiModel
public class SaveWorkflowNodeReq {

    @ApiModelProperty(value = "工作流ID", required = true)
    @NotNull(message = "{workflow.id.notNull}")
    @Positive(message = "{workflow.id.positive}")
    private Long workflowId;

    @ApiModelProperty(value = "项目ID", required = true)
    @NotNull(message = "{project.id.notNull}")
    @Positive(message = "{project.id.positive}")
    private Long projectId;

    @ApiModelProperty(value = "节点数", required = true)
    private Integer nodeNumber;

    @ApiModelProperty(value = "节点名称", required = true)
    private String nodeName;

}
