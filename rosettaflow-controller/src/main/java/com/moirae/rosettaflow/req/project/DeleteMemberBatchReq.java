package com.moirae.rosettaflow.req.project;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 批量删除项目成员请求参数
 *
 * @author houz
 */
@Data
@ApiModel(value = "批量删除项目成员请求参数")
public class DeleteMemberBatchReq {

    @ApiModelProperty(value = "项目成员ID字符串, 多个以逗号拼接", required = true)
    @NotBlank(message = "{project.member.id.notNull}")
    private String projMemberIds;

}
