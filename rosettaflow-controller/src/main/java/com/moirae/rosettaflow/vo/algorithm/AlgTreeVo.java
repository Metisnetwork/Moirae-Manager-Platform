package com.moirae.rosettaflow.vo.algorithm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "查询算法树详情列表响应参数")
public class AlgTreeVo {

    @ApiModelProperty(value = "算法详情")
    private List<AlgTreeItemVo> nodeList;

}
