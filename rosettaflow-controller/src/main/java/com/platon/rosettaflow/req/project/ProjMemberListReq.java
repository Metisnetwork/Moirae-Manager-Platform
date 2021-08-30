package com.platon.rosettaflow.req.project;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 查询项目成员列表请求参数
 * @author houz
 */
@Data
@ApiModel(value = "查询项目成员列表请求参数")
public class ProjMemberListReq {

    @ApiModelProperty(value = "项目ID", required = true)
    @NotNull(message = "{project.id.notBlank}")
    private Long projectId;

    @ApiModelProperty(value = "用户昵称")
    private String userName;


}
