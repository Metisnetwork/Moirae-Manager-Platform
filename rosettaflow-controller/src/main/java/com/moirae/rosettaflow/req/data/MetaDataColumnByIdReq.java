package com.moirae.rosettaflow.req.data;

import com.moirae.rosettaflow.req.CommonPageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "元数据列查询")
public class MetaDataColumnByIdReq extends CommonPageReq {

    @ApiModelProperty(value = "元数据id", required = true)
    @NotBlank(message = "{metadata.id.notNull}")
    private String metaDataId;
}
