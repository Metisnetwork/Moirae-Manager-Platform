package com.datum.platform.vo.data;

import com.datum.platform.vo.task.BaseOrgVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class DataVo extends BaseOrgVo {

    @ApiModelProperty(value = "元数据id")
    private String metaDataId;

    @ApiModelProperty(value = "元数据名称")
    private String metaDataName;

    @ApiModelProperty(value = "发布时间，精确到毫秒")
    private Date publishedAt;

    @ApiModelProperty(value = "源文件的大小 (单位: byte)")
    private Long size;

    @ApiModelProperty(value = "元数据所属行业  1：金融业（银行）、2：金融业（保险）、3：金融业（证券）、4：金融业（其他）、5：ICT、 6：制造业、 7：能源业、 8：交通运输业、 9 ：医疗健康业、 10 ：公共服务业、 11：传媒广告业、 12 ：其他行业'")
    private String industry;

    @ApiModelProperty(value = "是否支持明文算法: true-是 false-否")
    private Boolean isSupportPtAlg;
}
