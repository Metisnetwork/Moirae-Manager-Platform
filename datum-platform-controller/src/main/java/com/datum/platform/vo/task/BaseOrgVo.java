package com.datum.platform.vo.task;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BaseOrgVo {

    @ApiModelProperty(value = "组织身份id")
    private String identityId;

    @ApiModelProperty(value = "组织名称")
    private String nodeName;
}
