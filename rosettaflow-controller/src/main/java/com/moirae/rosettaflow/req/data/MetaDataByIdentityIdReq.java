package com.moirae.rosettaflow.req.data;

import com.moirae.rosettaflow.req.CommonPageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;


/**
 * @author hudenian
 * @date 2021/8/25
 * @description 元数据列表请求参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "元数据请求参数")
public class MetaDataByIdentityIdReq extends CommonPageReq {

    @ApiModelProperty(value = "组织的身份标识Id", required = true)
    @NotBlank(message = "{node.identity.id.NotBlank}")
    private String identityId;
}
