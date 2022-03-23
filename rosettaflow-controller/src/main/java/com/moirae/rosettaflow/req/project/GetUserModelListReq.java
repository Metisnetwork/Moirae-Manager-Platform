package com.moirae.rosettaflow.req.project;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 查询当前项目的算法模型请求参数
 * @author houz
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "查询当前用户的算法模型")
public class GetUserModelListReq {

    @ApiModelProperty(value = "组织ID不能为空", required = true)
    @NotNull(message = "{identityId.id.NotBlank}")
    private String identityId;

    @ApiModelProperty(value = "算法ID", required = true)
    @NotNull(message = "algorithm.id.notNull")
    private Long algorithmId;
}
