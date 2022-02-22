package com.moirae.rosettaflow.req.task;

import com.moirae.rosettaflow.req.CommonPageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "元数据列查询")
public class GetOrgTaskListByIdentityIdReq extends CommonPageReq {

    @ApiModelProperty(value = "组织的身份标识Id", required = true)
    @NotBlank(message = "{node.identity.id.NotBlank}")
    private String identityId;
}
