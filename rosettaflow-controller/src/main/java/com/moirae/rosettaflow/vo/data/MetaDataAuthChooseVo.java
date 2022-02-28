package com.moirae.rosettaflow.vo.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class MetaDataAuthChooseVo {

    @ApiModelProperty(value = "元数据id")
    private String metaDataId;

    @ApiModelProperty(value = "资源所属组织的身份标识Id")
    private String identityId;

    @ApiModelProperty(value = "元数据名称")
    private String metaDataName;

    @ApiModelProperty(value = "元数据名称")
    public String getDataName(){
       return metaDataName;
    }
}
