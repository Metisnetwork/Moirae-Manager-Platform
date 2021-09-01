package com.platon.rosettaflow.req.data;

import com.platon.rosettaflow.req.CommonPageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author hudenian
 * @date 2021/8/25
 * @description 元数据列表请求参数
 */
@Data
@ApiModel(value = "元数据请求参数")
public class MetaDataReq extends CommonPageReq {

    @ApiModelProperty(value = "元数据名称|数据名称 (表名)")
    private String dataName;
}
