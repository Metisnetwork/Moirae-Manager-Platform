package com.moirae.rosettaflow.req.data;

import com.moirae.rosettaflow.req.CommonPageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author hudenian
 * @date 2021/8/25
 * @description 元数据列表请求参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "元数据请求参数")
public class MetaDataReq extends CommonPageReq {

    @ApiModelProperty(value = "元数据名称|数据名称 (表名)")
    private String dataName;
}
