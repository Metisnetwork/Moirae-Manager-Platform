package com.moirae.rosettaflow.req.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author hudenian
 * @date 2021/9/10
 * @description 功能描述
 */
@Data
@ApiModel(value = "获取用户已授权的元数据表")
public class MetaDataAuthTablesReq {

    @ApiModelProperty(value = "组织的身份标识Id", required = true)
    @NotBlank(message = "{node.identity.id.NotBlank}")
    private String identityId;
}
