package com.platon.rosettaflow.req.project;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

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
    @NotBlank(message = "{page.number.notBlank}")
    private int current;

    @ApiModelProperty(value = "每页数据条数", required = true)
    @NotBlank(message = "{each.page.row.notBlank}")
    private int size;


}
