package com.platon.rosettaflow.req.project;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 添加项目请求参数
 *
 * @author houz
 */
@Data
@ApiModel(value = "添加项目请求参数")
public class AddProjectReq {

    @ApiModelProperty(value = "项目名称", required = true)
    @NotBlank(message = "{project.name.notBlank}")
    private String projectName;

    @ApiModelProperty(value = "项目描述", required = true)
    private String projectDesc;

    @ApiModelProperty(value = "项目模板ID")
    private Long projectTempId;

}
