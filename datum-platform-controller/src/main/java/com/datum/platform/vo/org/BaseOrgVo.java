package com.datum.platform.vo.org;

import com.datum.platform.mapper.enums.OrgStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class BaseOrgVo {

    @ApiModelProperty(value = "组织的身份名称")
    private String nodeName;

    @ApiModelProperty(value = "组织的身份标识Id")
    private String identityId;

    @ApiModelProperty(value = "组织的状态 状态,1-Normal; 2-NonNormal")
    private OrgStatusEnum status;

    @ApiModelProperty(value = "组织的最新更新时间")
    private Date updateAt;

    @ApiModelProperty(value = "是否为委员会成员")
    private Boolean isAuthority;

    @ApiModelProperty(value = "是否已认证")
    private Boolean isCertified;
}
