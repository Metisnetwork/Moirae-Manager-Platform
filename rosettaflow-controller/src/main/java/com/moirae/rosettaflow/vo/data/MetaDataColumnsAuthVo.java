package com.moirae.rosettaflow.vo.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hudenian
 * @date 2021/9/10
 * @description 元数据列详情
 */
@Data
@ApiModel
public class MetaDataColumnsAuthVo {

    @ApiModelProperty(value = "数据详情表ID")
    private Long id;

    @ApiModelProperty(value = "元数据id")
    private String metaDataId;

    @ApiModelProperty(value = "列索引")
    private Integer columnIndex;

    @ApiModelProperty(value = "列名")
    private String columnName;

}
