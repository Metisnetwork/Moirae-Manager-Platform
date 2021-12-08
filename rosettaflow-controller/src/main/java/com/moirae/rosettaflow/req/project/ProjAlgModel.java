package com.moirae.rosettaflow.req.project;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 查询当前项目下当前算法的模型请求参数
 * @author houz
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "查询当前项目的算法模型请求参数")
public class ProjAlgModel {

    @ApiModelProperty(value = "项目ID", required = true)
    @NotNull(message = "project.id.notNull")
    private Long projectId;

    @ApiModelProperty(value = "项目ID", required = true)
    @NotNull(message = "algorithm.id.notNull")
    private Long algorithmId;
}
