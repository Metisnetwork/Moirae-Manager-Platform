package com.platon.rosettaflow.req.project.temp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 添加项目模板请求参数
 *
 * @author houz
 */
@Data
@ApiModel(value = "添加项目模板请求参数")
public class AddProjectTemplateReq {

    @ApiModelProperty(value = "工作流ID", required = true)
    @NotNull(message = "{workflow.id.notNull}")
    private Long id;

}
