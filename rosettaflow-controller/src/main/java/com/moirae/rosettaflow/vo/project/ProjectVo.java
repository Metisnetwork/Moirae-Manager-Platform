package com.moirae.rosettaflow.vo.project;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * 创建项目返回项目id
 * @author houz
 */
@Data
@AllArgsConstructor
@ApiModel(value = "项目详情返回参数")
public class ProjectVo {

    @ApiModelProperty(value = "项目ID")
    private Long id;


}
