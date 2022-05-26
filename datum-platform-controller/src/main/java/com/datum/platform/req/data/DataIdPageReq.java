package com.datum.platform.req.data;

import com.datum.platform.req.CommonPageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;


@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "元数据列表字段请求参数")
public class DataIdPageReq extends CommonPageReq {

    @ApiModelProperty(value = "元数据列表字段metaDataId", required = true)
    @NotNull(message = "{metadata.metadataid.notNull}")
    private String metaDataId;
}
