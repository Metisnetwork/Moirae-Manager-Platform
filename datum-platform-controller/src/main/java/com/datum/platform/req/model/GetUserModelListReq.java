package com.datum.platform.req.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "查询当前用户的算法模型")
public class GetUserModelListReq {

    @ApiModelProperty(value = "组织ID不能为空", required = true)
    @NotNull(message = "{identityId.id.NotBlank}")
    private String identityId;

    @ApiModelProperty(value = "算法ID", required = true)
    @NotNull(message = "algorithm.id.notNull")
    private Long algorithmId;
}
