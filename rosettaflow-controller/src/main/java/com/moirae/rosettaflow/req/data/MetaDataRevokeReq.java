package com.moirae.rosettaflow.req.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;


/**
 * @author hudenian
 * @date 2021/8/27
 * @description 撤销元数据授权
 */
@Data
@ApiModel(value = "撤销元数据授权对象")
public class MetaDataRevokeReq {

    @ApiModelProperty(value = "用户授权数据表Id")
    @NotBlank(message = "{metadata.usermetadataid.notNull}")
    @Positive(message = "{metadata.usermetadataid.positive}")
    private Long id;

    @ApiModelProperty(value = "用户钱包地址", required = true)
    @NotBlank(message = "{user.address.notBlank}")
    private String address;

    @ApiModelProperty(value = "元数据授权申请Id", required = true)
    @NotBlank(message = "{user.address.notBlank}")
    private String metadataAuthId;

    @ApiModelProperty(value = "撤销数据授权申请的账户的签名", required = true)
    @NotNull(message = "{metadata.revoke.sign.notNull}")
    private String sign;

}
