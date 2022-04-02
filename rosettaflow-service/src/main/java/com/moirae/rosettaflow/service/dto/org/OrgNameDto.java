package com.moirae.rosettaflow.service.dto.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "组织名称")
public class OrgNameDto {

    @ApiModelProperty(value = "组织身份id")
    private String identityId;

    @ApiModelProperty(value = "组织名称")
    private String nodeName;
}
