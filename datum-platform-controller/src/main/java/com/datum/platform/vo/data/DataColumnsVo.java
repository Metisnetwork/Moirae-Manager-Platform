package com.datum.platform.vo.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "元数据列")
public class DataColumnsVo {

    @ApiModelProperty(value = "列索引")
    private Integer columnIdx;

    @ApiModelProperty(value = "列名")
    private String columnName;

    @ApiModelProperty(value = "字段类型")
    private String columnType;

    @ApiModelProperty(value = "字段备注")
    private String remarks;
}
