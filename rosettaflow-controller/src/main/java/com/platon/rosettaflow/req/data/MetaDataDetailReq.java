package com.platon.rosettaflow.req.data;

import com.platon.rosettaflow.req.CommonPageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * @author hudenian
 * @date 2021/8/25
 * @description 元数据详情请求参数
 */
@Data
@ApiModel(value = "元数据详情请求参数")
public class MetaDataDetailReq extends CommonPageReq {

    @ApiModelProperty(value = "元数据表id", required = true)
    @NotNull(message = "{metadata.id.notNull}")
    @Positive(message = "{metadata.id.positive}")
    private Long id;

}
