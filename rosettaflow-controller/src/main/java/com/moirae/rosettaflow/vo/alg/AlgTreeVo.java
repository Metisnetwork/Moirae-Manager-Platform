package com.moirae.rosettaflow.vo.alg;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "查询算法树详情响应参数")
public class AlgTreeVo {

    @ApiModelProperty(value = "算法或分类id")
    private Integer id;

    @ApiModelProperty(value = "算法或分类名称")
    private String name;

    @ApiModelProperty(value = "算法或分类名称")
    private String nameEn;

    @ApiModelProperty(value = "是否算法")
    private Boolean isAlg;

    @ApiModelProperty(value = "是否存在对应算法")
    private Boolean isExistAlg;

    @ApiModelProperty(value = "算法明细")
    private AlgVo alg;

    @ApiModelProperty(value = "明细")
    private List<AlgTreeVo> childrenList;
}
