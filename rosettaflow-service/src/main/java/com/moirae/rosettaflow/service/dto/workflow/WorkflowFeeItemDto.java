package com.moirae.rosettaflow.service.dto.workflow;

import com.moirae.rosettaflow.common.enums.WorkflowPayTypeEnum;
import com.moirae.rosettaflow.service.dto.token.TokenDto;
import com.moirae.rosettaflow.service.dto.token.TokenHolderDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "工作流列表响应对象")
public class WorkflowFeeItemDto {

    @ApiModelProperty(value = "支付类型（手续费、token）")
    private WorkflowPayTypeEnum type;

    @ApiModelProperty(value = "需消耗的金额")
    private String needValue;

    @ApiModelProperty(value = "是否足够")
    private Boolean isEnough;

    @ApiModelProperty(value = "ERC20代币信息")
    private TokenDto token;

    @ApiModelProperty(value = "ERC20代币信息")
    private TokenHolderDto tokenHolder;
}
