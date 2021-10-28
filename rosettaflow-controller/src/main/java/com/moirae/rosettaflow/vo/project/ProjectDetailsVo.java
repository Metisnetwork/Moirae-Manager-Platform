package com.moirae.rosettaflow.vo.project;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 项目详情返回参数
 * @author houz
 */
@Data
@ApiModel(value = "项目详情返回参数")
public class ProjectDetailsVo {

    @ApiModelProperty(value = "项目ID")
    private Long id;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "项目描述")
    private String projectDesc;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

}
