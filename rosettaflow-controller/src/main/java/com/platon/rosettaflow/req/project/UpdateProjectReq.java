package com.platon.rosettaflow.req.project;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 修改项目请求参数
 * @author houz
 */
@Data
@ApiModel(value = "修改项目请求参数")
public class UpdateProjectReq {

    @ApiModelProperty(value = "项目ID", required = true)
    @NotNull(message = "{project.id.notNull}")
    private Long id;

    @ApiModelProperty(value = "项目名称", required = true)
    @NotBlank(message = "{project.name.notBlank}")
    private String projectName;

    @ApiModelProperty(value = "项目描述", required = true)
    @NotBlank(message = "{project.describe.notBlank}")
    private String projectDesc;

}
