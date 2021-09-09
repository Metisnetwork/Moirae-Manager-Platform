package com.platon.rosettaflow.req.data;

import com.platon.rosettaflow.req.CommonPageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author hudenian
 * @date 2021/8/25
 * @description 元数据列表字段
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "元数据列表字段请求参数")
public class MetaDataDetailReq extends CommonPageReq {

    @ApiModelProperty(value = "元数据列表字段metaDataId", required = true)
    @NotNull(message = "{metadata.metadataid.notNull}")
    private String metaDataId;

}
