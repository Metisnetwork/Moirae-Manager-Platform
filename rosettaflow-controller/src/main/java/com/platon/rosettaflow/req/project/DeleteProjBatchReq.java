package com.platon.rosettaflow.req.project;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 批量删除项目请求参数
 * @author houz
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "批量删除项目请求参数")
public class DeleteProjBatchReq {

    @ApiModelProperty(value = "项目ID字符串, 多个项目id逗号分隔", required = true)
    @NotBlank(message = "project.id.notNull")
    private String ids;

}
