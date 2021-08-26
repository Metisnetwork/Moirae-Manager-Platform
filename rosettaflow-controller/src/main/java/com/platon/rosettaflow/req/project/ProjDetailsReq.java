package com.platon.rosettaflow.req.project;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 查询项目详情请求参数
 * @author houz
 */
@Data
@ApiModel(value = "查询项目详情请求参数")
public class ProjDetailsReq {

    @ApiModelProperty(value = "项目ID", example = "", required = true)
    @NotNull(message = "项目ID不能为空")
    private Long id;

}
