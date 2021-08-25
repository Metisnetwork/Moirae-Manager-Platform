package com.platon.rosettaflow.req.project;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 保存项目信息请求参数
 * @author houz
 */
@Data
@ApiModel(value = "保存项目信息请求参数")
public class SaveProjectReq {

    @ApiModelProperty(value = "项目ID(自增长)", example = "")
    private Long id;

    @ApiModelProperty(value = "用户id(创建者id)", example = "", required = true)
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @ApiModelProperty(value = "项目名称", example = "", required = true)
    @NotNull(message = "项目名称不能为空")
    private String projectName;

    @ApiModelProperty(value = "项目描述", example = "", required = true)
    @NotNull(message = "项目描述不能为空")
    private String projectDesc;

}
