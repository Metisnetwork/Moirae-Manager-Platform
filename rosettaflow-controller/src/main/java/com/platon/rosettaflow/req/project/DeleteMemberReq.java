package com.platon.rosettaflow.req.project;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 删除项目成员请求参数
 * @author houz
 */
@Data
@ApiModel(value = "删除项目成员请求参数")
public class DeleteMemberReq {

    @ApiModelProperty(value = "项目成员ID", required = true)
    @NotNull(message = "{project.member.id.notNull}")
    private Long projMemberId;

}
