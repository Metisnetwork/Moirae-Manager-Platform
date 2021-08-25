package com.platon.rosettaflow.req.project;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 查询项目列表请求参数
 * @author houz
 */
@Data
@ApiModel(value = "查询项目列表请求参数")
public class ProjListReq {

    @ApiModelProperty(value = "用户id(创建者id)", example = "", required = true)
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @ApiModelProperty(value = "项目名称", example = "", required = true)
    private String projectName;

    @NotNull(message = "起始页号不能为空")
    @ApiModelProperty(value = "起始页号", example = "", required = true)
    private int pageNumber;

    @NotNull(message = "每页数据条数不能为空")
    @ApiModelProperty(value = "每页数据条数", example = "", required = true)
    private int pageSize;


}
