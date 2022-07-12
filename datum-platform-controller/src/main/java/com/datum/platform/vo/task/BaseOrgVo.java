package com.datum.platform.vo.task;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BaseOrgVo {

    @ApiModelProperty(value = "组织身份id")
    private String identityId;

    @ApiModelProperty(value = "组织名称")
    private String nodeName;

    @ApiModelProperty(value = "是否为委员会成员")
    private Boolean isAuthority;

    @ApiModelProperty(value = "是否已认证")
    private Boolean isCertified;
}
