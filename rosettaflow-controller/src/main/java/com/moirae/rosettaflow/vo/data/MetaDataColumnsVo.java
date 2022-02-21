package com.moirae.rosettaflow.vo.data;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author hudenian
 * @date 2021/8/26
 * @description 元数据列详情
 */
@Data
@ApiModel
public class MetaDataColumnsVo {

    @ApiModelProperty(value = "数据详情表ID")
    private Long id;

    @ApiModelProperty(value = "元数据id")
    private String metaDataId;

    @ApiModelProperty(value = "列索引")
    private Integer columnIndex;

    @ApiModelProperty(value = "列名")
    private String columnName;

    @ApiModelProperty(value = "列类型")
    private String columnType;

    @ApiModelProperty(value = "列大小（byte）")
    private Long columnSize;

    @ApiModelProperty(value = "列描述")
    private String columnDesc;

    @ApiModelProperty(value = "状态: 0-无效，1- 有效")
    @TableField(value = "`status`")
    private Byte status;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "列索引")
    private Integer columnIdx;

    @ApiModelProperty(value = "列描述")
    private String remarks;

}
