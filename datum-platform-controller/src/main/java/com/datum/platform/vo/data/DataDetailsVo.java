package com.datum.platform.vo.data;

import com.datum.platform.mapper.enums.MetaDataFileTypeEnum;
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

    @ApiModelProperty(value = "元数据所属行业  1：金融业（银行）、2：金融业（保险）、3：金融业（证券）、4：金融业（其他）、5：ICT、 6：制造业、 7：能源业、 8：交通运输业、 9 ：医疗健康业、 10 ：公共服务业、 11：传媒广告业、 12 ：其他行业'")
    private String industry;

    @ApiModelProperty(value = "源文件的大小 (单位: byte)")
    private Long size;

    @ApiModelProperty(value = "源文件的行数")
    private Integer rows;

    @ApiModelProperty(value = "源文件的列数")
    private Integer columns;

    @ApiModelProperty(value = "数据描述")
    private String remarks;

    @ApiModelProperty(value = "数据列")
    private List<DataColumnsVo> columnsList;
}
