package com.moirae.rosettaflow.req.project;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 查询项目详情请求参数
 * @author houz
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "查询项目详情请求参数")
public class ProjDetailsReq {

    @ApiModelProperty(value = "项目ID", required = true)
    @NotNull(message = "project.id.notNull")
    private Long id;

}
