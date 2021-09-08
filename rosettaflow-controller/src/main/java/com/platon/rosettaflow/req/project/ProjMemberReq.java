package com.platon.rosettaflow.req.project;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 保存项目成员请求参数
 * @author houz
 */
@Data
@ApiModel(value = "保存项目成员请求参数")
public class ProjMemberReq {

    @ApiModelProperty(value = "项目ID", required = true)
    @NotNull(message = "{project.id.NotNull}")
    private Long projectId;

    @ApiModelProperty(value = "用户ID")
    @NotNull(message = "{user.id.NotNull}")
    private Long userId;

    @ApiModelProperty(value = "成员角色(1-管理员，2-编辑着, 3-查看着)")
    @NotNull(message = "{project.member.role.NotNull}")
    private Integer role;


}
