package com.moirae.rosettaflow.req.workflow;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@ApiModel
public class StartWorkflowReq {

    @ApiModelProperty(value = "工作流ID", required = true)
    @NotNull(message = "{workflow.id.notNull}")
    @Positive(message = "{workflow.id.positive}")
    private Long workflowId;

    @ApiModelProperty(value = "工作流版本", required = true)
    private Integer workflowVersion;

    @ApiModelProperty(value = "用户钱包地址", required = true)
    @NotBlank(message = "{user.address.notBlank}")
    private String address;

    @ApiModelProperty(value = "发起任务的账户的签名", required = true)
    @NotBlank(message = "{user.sign.notBlank}")
    private String sign;
}
