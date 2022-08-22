package com.datum.platform.service.dto.workflow;

import com.datum.platform.common.enums.WorkflowPayTypeEnum;
import com.datum.platform.service.dto.token.TokenDto;
import com.datum.platform.service.dto.token.TokenHolderDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class WorkflowFeeItemDto {

    @ApiModelProperty(value = "支付类型（手续费、token）")
    private WorkflowPayTypeEnum type;

    @ApiModelProperty(value = "需消耗的数量")
    private String needValue;

    @ApiModelProperty(value = "是否足够")
    private Boolean isEnough;

    @ApiModelProperty(value = "ERC20代币信息")
    private TokenDto token;

    @ApiModelProperty(value = "ERC20代币信息")
    private TokenHolderDto tokenHolder;
}
