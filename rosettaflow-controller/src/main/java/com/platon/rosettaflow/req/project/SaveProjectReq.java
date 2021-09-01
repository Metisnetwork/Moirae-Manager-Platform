package com.platon.rosettaflow.req.project;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 保存项目信息请求参数
 * @author houz
 */
@Data
@ApiModel(value = "保存项目信息请求参数")
public class SaveProjectReq {

    @ApiModelProperty(value = "项目ID(自增长)")
    private Long id;

    @ApiModelProperty(value = "用户id(创建者id)", required = true)
    @NotNull(message = "{user.id.notBlank}")
    private Long userId;

    @ApiModelProperty(value = "项目名称", required = true)
    @NotBlank(message = "{project.name.notBlank}")
    private String projectName;

    @ApiModelProperty(value = "项目描述", required = true)
    @NotBlank(message = "{project.describe.notBlank}")
    private String projectDesc;

}
