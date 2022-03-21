package com.moirae.rosettaflow.req.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "数据查询")
public class GetDataDetailsReq {

    @ApiModelProperty(value = "元数据列表字段metaDataId", required = true)
    @NotNull(message = "{metadata.metadataid.notNull}")
    private String metaDataId;
}
