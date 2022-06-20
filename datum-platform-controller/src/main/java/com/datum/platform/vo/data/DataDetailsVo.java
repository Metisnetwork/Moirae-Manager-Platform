package com.datum.platform.vo.data;

import com.datum.platform.mapper.enums.MetaDataFileTypeEnum;
import com.datum.platform.service.dto.data.AttributeCredentialDto;
import com.datum.platform.service.dto.data.NoAttributeCredentialDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "元数据详情")
public class DataDetailsVo extends DataVo {

    @ApiModelProperty(value = "源文件类型: 0-未知，1- CSV类型")
    private MetaDataFileTypeEnum fileType;

    @ApiModelProperty(value = "数据凭证符号")
    private String tokenSymbol;

    @ApiModelProperty(value = "发布时间，精确到毫秒")
    private Date publishedAt;

    @ApiModelProperty(value = "源文件的行数")
    private Integer rows;

    @ApiModelProperty(value = "源文件的列数")
    private Integer columns;

    @ApiModelProperty(value = "数据描述")
    private String remarks;

    @ApiModelProperty(value = "数据列")
    private List<DataColumnsVo> columnsList;

    @ApiModelProperty(value = "无属性凭证")
    private NoAttributeCredentialDto noAttributeCredential;

    @ApiModelProperty(value = "有属性凭证列表")
    private List<AttributeCredentialDto> attributeCredentialList;
}
