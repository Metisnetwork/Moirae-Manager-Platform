package com.moirae.rosettaflow.vo.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "元数据列信息")
public class MetaDataColumnsVo extends MetaDataColumnsChooseVo{

    @ApiModelProperty(value = "列类型")
    private String columnType;

    @ApiModelProperty(value = "列描述")
    private String remarks;

    @ApiModelProperty(value = "列描述-后期调整为remarks")
    private String getColumnDesc(){
        return remarks;
    }
}
