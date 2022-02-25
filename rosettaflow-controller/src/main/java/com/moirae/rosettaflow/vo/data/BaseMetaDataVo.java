package com.moirae.rosettaflow.vo.data;

import com.moirae.rosettaflow.mapper.enums.MetaDataStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class BaseMetaDataVo {

    @ApiModelProperty(value = "元数据id")
    private String metaDataId;

    @ApiModelProperty(value = "元数据名称")
    private String metaDataName;

    @ApiModelProperty(value = "源文件类型: 0-未知，1- CSV类型")
    private Byte fileType;

    @ApiModelProperty(value = "源文件的大小 (单位: byte)")
    private Long size;

    @ApiModelProperty(value = "发布时间，精确到毫秒")
    private Date publishedAt;

    @ApiModelProperty(value = "元数据的状态 (0: 未知; 1: 还未发布的新表; 2: 已发布的表; 3: 已撤销的表)")
    private MetaDataStatusEnum status;

    @ApiModelProperty(value = "(状态)修改时间")
    private Date updateAt;

    @ApiModelProperty(value = "发布时间，精确到毫秒")
    public Date getPublishAt(){
        return publishedAt;
    }

    @ApiModelProperty(value = "(状态)修改时间")
    public Date getUpdateTime(){
        return updateAt;
    }
}
