package com.datum.platform.vo.data;

import com.datum.platform.vo.task.BaseOrgVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "元数据信息")
public class DataVo extends BaseOrgVo {

    @ApiModelProperty(value = "元数据id")
    private String metaDataId;

    @ApiModelProperty(value = "元数据名称")
    private String metaDataName;

    @ApiModelProperty(value = "发布时间，精确到毫秒")
    private Date publishedAt;

    @ApiModelProperty(value = "源文件的大小 (单位: byte)")
    private Long size;

    @ApiModelProperty(value = "是否支持明文算法: true-是 false-否")
    private Boolean isSupportPtAlg;
}
