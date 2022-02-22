package com.moirae.rosettaflow.vo.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "算了提供方")
public class PowerProviderVo extends BaseOrgVo {

    @ApiModelProperty(value = "使用的内存")
    private Long usedMemory;

    @ApiModelProperty(value = "使用的带宽")
    private Long usedBandwidth;

    @ApiModelProperty(value = "使用的CPU核数")
    private Integer usedCore;
}
