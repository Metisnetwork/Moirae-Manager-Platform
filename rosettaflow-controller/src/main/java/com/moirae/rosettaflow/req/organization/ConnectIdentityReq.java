package com.moirae.rosettaflow.req.organization;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * 用户连接组织请求参数
 *
 * @author hudenian
 * @date 2021/12/15
 */
@Data
@ApiModel(value = "用户连接组织请求参数")
public class ConnectIdentityReq {

    @ApiModelProperty(value = "用户组织连接绑定关系表ID", required = true)
    @NotBlank(message = "{identityId.id.NotBlank}")
    private String identityId;
}
