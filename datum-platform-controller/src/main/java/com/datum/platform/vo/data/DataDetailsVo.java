package com.datum.platform.vo.data;

import com.datum.platform.mapper.enums.MetaDataFileTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "元数据详情")
public class DataDetailsVo extends DataVo {

    @ApiModelProperty(value = "源文件类型: 0-未知，1- CSV类型")
    private MetaDataFileTypeEnum fileType;

    @ApiModelProperty(value = "源文件的行数")
    private Integer rows;

    @ApiModelProperty(value = "源文件的列数")
    private Integer columns;

    @ApiModelProperty(value = "数据描述")
    private String remarks;

    @ApiModelProperty(value = "数据列")
    private List<DataColumnsVo> columnsList;
}
