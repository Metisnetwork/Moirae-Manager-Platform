package com.moirae.rosettaflow.req.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 删除ip及port与组织绑定关系请求对象
 *
 * @author hudenian
 * @date 2021/12/15
 */
@Data
@ApiModel(value = "组织标识")
public class OrgIdReq {

    @ApiModelProperty(value = "组织ID不能为空", required = true)
    @NotNull(message = "{identityId.id.NotBlank}")
    private String identityId;
}
