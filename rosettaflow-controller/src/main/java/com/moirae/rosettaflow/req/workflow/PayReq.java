package com.moirae.rosettaflow.req.workflow;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@ApiModel
public class PayReq {

    @ApiModelProperty(value = "工作流ID", required = true)
    @NotNull(message = "{workflow.id.notNull}")
    @Positive(message = "{workflow.id.positive}")
    private Long workflowId;

    @ApiModelProperty(value = "工作流版本", required = true)
    private Integer version;

    @ApiModelProperty(value = "支付类型（手续费、token）")
    private String type;

    @ApiModelProperty(value = "token-元数据id")
    private String metaDataId;

    @ApiModelProperty(value = "签名后的消息")
    private String signedMessage;
}
