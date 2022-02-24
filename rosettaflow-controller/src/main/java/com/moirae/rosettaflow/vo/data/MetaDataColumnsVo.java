package com.moirae.rosettaflow.vo.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class MetaDataColumnsVo {

    @ApiModelProperty(value = "元数据id")
    private String metaDataId;

    @ApiModelProperty(value = "列索引")
    private Integer columnIdx;

    @ApiModelProperty(value = "列名")
    private String columnName;

    @ApiModelProperty(value = "列类型")
    private String columnType;

    @ApiModelProperty(value = "列描述")
    private String remarks;
}
