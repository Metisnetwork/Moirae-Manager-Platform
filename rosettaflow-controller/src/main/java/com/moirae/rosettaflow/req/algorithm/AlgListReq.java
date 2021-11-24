package com.moirae.rosettaflow.req.algorithm;

import com.moirae.rosettaflow.req.CommonPageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author houz
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "算法列表请求参数")
public class AlgListReq extends CommonPageReq {

    @ApiModelProperty(value = "算法名称")
    private String algorithmName;

}
