package com.platon.rosettaflow.vo.algorithm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author houz
 */
@Data
@ApiModel(value = "查询算法树详情列表响应参数")
public class AlgTreeListVo {

    @ApiModelProperty(value = "算法详情")
    private List<AlgTreeVo> algTreeVoList;

}
