package com.moirae.rosettaflow.req.data;

import com.moirae.rosettaflow.req.CommonPageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "任务列表查询")
public class GetDataListByIdentityIdReq extends CommonPageReq {

    @ApiModelProperty(value = "组织相关的（组织关联元数据）")
    private String identityId;
}
