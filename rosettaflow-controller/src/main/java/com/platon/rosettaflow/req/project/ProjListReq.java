package com.platon.rosettaflow.req.project;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 查询项目列表请求参数
 * @author houz
 */
@Data
@ApiModel(value = "查询项目列表请求参数")
public class ProjListReq {

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "起始页号", required = true)
    @NotNull(message = "{page.number.notBlank}")
    private Integer current;

    @ApiModelProperty(value = "每页数据条数", required = true)
    @NotNull(message = "{each.page.row.notBlank}")
    private Integer size;


}
