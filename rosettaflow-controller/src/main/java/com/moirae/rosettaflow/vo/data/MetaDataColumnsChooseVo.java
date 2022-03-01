package com.moirae.rosettaflow.vo.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "工作流输入元数据列")
public class MetaDataColumnsChooseVo {

    @ApiModelProperty(value = "元数据id")
    private String metaDataId;

    @ApiModelProperty(value = "列索引")
    private Integer columnIdx;

    @ApiModelProperty(value = "列名")
    private String columnName;

    @ApiModelProperty(value = "列索引-后期调整为 columnIdx")
    public Integer getColumnIndex(){
        return columnIdx;
    }

    @ApiModelProperty(value = "列标识-后期调整为 columnIdx")
    public Integer getId(){
        return columnIdx;
    }
}
