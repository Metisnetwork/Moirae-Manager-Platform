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
 * @description 添加工作流请求对象
 */
@Data
@ApiModel
public class AddWorkflowReq {

    @ApiModelProperty(value = "项目id", required = true)
    @NotNull(message = "{project.id.notNull}")
    @Positive(message = "{project.id.positive}")
    private Long projectId;

    @ApiModelProperty(value = "工作流名称", required = true)
    @NotBlank(message = "{workflow.id.notNull}")
    @Length(max = 30, message = "{workflow.name.Length}")
    private String workflowName;

    @ApiModelProperty(value = "工作流描述")
    @Length(max = 200, message = "{workflow.desc.Length}")
    private String workflowDesc;

}
