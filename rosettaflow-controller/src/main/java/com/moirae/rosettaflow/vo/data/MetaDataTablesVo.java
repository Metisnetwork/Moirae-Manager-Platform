package com.moirae.rosettaflow.vo.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hudenian
 * @date 2021/8/25
 * @description 用户授权元数据表
 */
@Data
@ApiModel
public class MetaDataTablesVo {

    @ApiModelProperty(value = "元数据表ID")
    private Long id;

    @ApiModelProperty(value = "元数据id")
    private String metaDataId;

    @ApiModelProperty(value = "资源所属组织的身份标识Id")
    private String identityId;

    @ApiModelProperty(value = "元数据名称|数据名称 (表名)")
    private String dataName;

}
