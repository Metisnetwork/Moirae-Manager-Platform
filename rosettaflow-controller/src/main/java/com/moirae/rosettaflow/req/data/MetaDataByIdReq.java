package com.moirae.rosettaflow.req.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "元数据详情查询")
public class MetaDataByIdReq {

    @ApiModelProperty(value = "元数据id", required = true)
    @NotBlank(message = "{metadata.id.notNull}")
    private String metaDataId;
}
