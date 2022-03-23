package com.moirae.rosettaflow.vo.workflow;

import com.moirae.rosettaflow.common.enums.WorkflowPayStatusEnum;
import com.moirae.rosettaflow.common.enums.WorkflowPayTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "工作流列表响应对象")
public class WorkflowPayVo {

    @ApiModelProperty(value = "工作流ID")
    private Long workflowId;

    @ApiModelProperty(value = "工作流版本")
    private Integer workflowVersion;

    @ApiModelProperty(value = "支付类型（手续费、token）")
    private WorkflowPayTypeEnum type;

    @ApiModelProperty(value = "支付的目标地址（手续费-发起组织的carrier地址、 token-dataToken拥有者地址）")
    private String to;

    @ApiModelProperty(value = "手续费-gasPrice定义")
    private String gasPrice;

    @ApiModelProperty(value = "手续费-推荐的gasPrice")
    private String recommendGasPrice;

    @ApiModelProperty(value = "冻结的金额（手续费-lat、 token-token）")
    private String freezeValue;

    @ApiModelProperty(value = "预估需要冻结的金额（手续费-lat、 token-token）")
    private String estimateFreezeValue;

    @ApiModelProperty(value = "token-数据凭证余额")
    private String tokenBalance;

    @ApiModelProperty(value = "lat的余额")
    private String balance;

    @ApiModelProperty(value = "token-数据凭证符号")
    private String tokenSymbol;

    @ApiModelProperty(value = "token-数据凭证精度")
    private Long tokenDecimal;

    @ApiModelProperty(value = "token-token合约地址")
    private String tokenAddress;

    @ApiModelProperty(value = "token-token合约名称")
    private String tokenName;

    @ApiModelProperty(value = "token-元数据id")
    private String metaDataId;

    @ApiModelProperty(value = "支付状态（待支付、 支付已提交、 支付成功、 支付失败、 撤销已提交、 撤销成功、 撤销失败）")
    private WorkflowPayStatusEnum status;
}
