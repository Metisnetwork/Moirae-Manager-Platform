package com.moirae.rosettaflow.vo.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "组织信息")
public class UserOrgVo extends BaseOrgVo{

    @ApiModelProperty(value = "组织的ip")
    private String identityIp;

    @ApiModelProperty(value = "组织的端口")
    private Integer identityPort;

    @ApiModelProperty(value = "是否公共可看的：0-否，1-是")
    private Byte publicFlag;
}
