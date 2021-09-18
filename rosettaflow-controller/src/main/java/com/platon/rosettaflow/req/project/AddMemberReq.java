package com.platon.rosettaflow.req.project;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * 保存项目成员请求参数
 * @author houz
 */
@Data
@ApiModel(value = "新增项目成员请求参数")
public class AddMemberReq {

    @ApiModelProperty(value = "项目成员ID", required = true)
    @NotNull(message = "{project.id.notNull}")
    @Positive(message = "{project.id.positive}")
    private Long projectId;

    @ApiModelProperty(value = "用户ID")
    @NotNull(message = "{user.id.NotNull}")
    @Positive(message = "{user.id.positive}")
    private Long userId;

    @ApiModelProperty(value = "成员角色(1-管理员，2-编辑着, 3-查看着)")
    @NotNull(message = "{project.member.role.NotNull}")
    private Integer role;


}
