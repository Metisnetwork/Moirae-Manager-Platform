package com.datum.platform.service.dto.workflow;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class WorkflowStartSignatureDto extends WorkflowVersionKeyDto {

    @ApiModelProperty(value = "发起任务的账户的签名", required = true)
    @NotBlank(message = "{user.sign.notBlank}")
    private String sign;

    @ApiModelProperty(value = "发起任务选择的凭证列表", required = true)
    private List<Long> credentialIdList;
}
