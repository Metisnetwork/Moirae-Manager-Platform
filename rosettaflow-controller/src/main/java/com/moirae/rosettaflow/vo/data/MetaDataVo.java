package com.moirae.rosettaflow.vo.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "元数据信息")
public class MetaDataVo extends BaseMetaDataVo {

    @ApiModelProperty(value = "参与任务的数据")
    private Integer taskCount;


}
