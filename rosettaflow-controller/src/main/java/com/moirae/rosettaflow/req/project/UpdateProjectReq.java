package com.moirae.rosettaflow.req.project;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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

    @ApiModelProperty(value = "项目描述")
    @Length(message = "{project.desc.maxSize}", max = 200)
    private String projectDesc;
}
