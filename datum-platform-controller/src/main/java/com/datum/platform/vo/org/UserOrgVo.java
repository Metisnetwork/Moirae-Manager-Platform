package com.datum.platform.vo.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "用户相关的组织信息")
public class UserOrgVo extends BaseOrgVo{

    @ApiModelProperty(value = "组织的ip")
    private String identityIp;

    @ApiModelProperty(value = "组织的端口")
    private Integer identityPort;

    @ApiModelProperty(value = "当前组织内置系统钱包地址 (见证人代理钱包)")
    private String observerProxyWalletAddress;

    @ApiModelProperty(value = "是否设置白名单")
    private Boolean isInWhitelist;

    @ApiModelProperty(value = "是否公共可看的：0-否，1-是")
    private Integer publicFlag;
}
