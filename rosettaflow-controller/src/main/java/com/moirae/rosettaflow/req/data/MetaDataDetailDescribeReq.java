package com.moirae.rosettaflow.req.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * @author hudenian
 * @date 2021/8/25
 * @description 元数据详情简介请求参数
 */
@Data
@ApiModel(value = "元数据详情简介请求参数")
public class MetaDataDetailDescribeReq {

    @ApiModelProperty(value = "元数据表id", required = true)
    @NotNull(message = "{metadata.id.notNull}")
    @Positive(message = "{metadata.id.positive}")
    private Long metaDataPkId;

    @ApiModelProperty(value = "用户授权数据表id")
    private Long userMetaDataId;

}
