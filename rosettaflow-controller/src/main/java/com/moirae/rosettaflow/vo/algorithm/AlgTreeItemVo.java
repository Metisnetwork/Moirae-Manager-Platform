package com.moirae.rosettaflow.vo.algorithm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author houz
 */
@Data
@ApiModel(value = "查询算法树详情响应参数")
public class AlgTreeItemVo {

    @ApiModelProperty(value = "算法或分类id")
    private Integer id;

    @ApiModelProperty(value = "算法或分类名称")
    private String name;

    @ApiModelProperty(value = "是否算法")
    private Boolean isAlg;

    @ApiModelProperty(value = "算法明细")
    private List<AlgVo> algItem;

    @ApiModelProperty(value = "明细")
    private List<AlgTreeItemVo> child;
}
