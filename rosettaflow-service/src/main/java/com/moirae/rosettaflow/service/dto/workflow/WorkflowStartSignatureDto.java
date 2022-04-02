package com.moirae.rosettaflow.service.dto.workflow;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class WorkflowStartSignatureDto extends WorkflowVersionKeyDto {

    @ApiModelProperty(value = "用户钱包地址", required = true)
    @NotBlank(message = "{user.address.notBlank}")
    private String address;

    @ApiModelProperty(value = "发起任务的账户的签名", required = true)
    @NotBlank(message = "{user.sign.notBlank}")
    private String sign;
}
