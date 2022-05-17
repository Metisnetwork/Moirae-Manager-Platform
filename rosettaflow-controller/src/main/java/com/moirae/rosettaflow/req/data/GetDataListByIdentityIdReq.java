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

    @ApiModelProperty(value = "搜索关键字(凭证名称或元数据名称关键字)")
    private String keyword;
}
