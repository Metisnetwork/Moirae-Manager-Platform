package com.platon.rosettaflow.vo.project;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hudenian
 * @date 2021/8/16
 * @description 功能描述
 */
@Data
@ApiModel
public class ProjectTemplateVo {

    @ApiModelProperty("项目模板表ID")
    private Long id;

    @ApiModelProperty("项目名称")
    private String projectName;

    @ApiModelProperty("项目描述")
    private String projectDesc;
}
