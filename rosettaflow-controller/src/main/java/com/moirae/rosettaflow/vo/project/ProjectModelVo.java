package com.moirae.rosettaflow.vo.project;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 查询当前项目下面生成的所有模型列表
 *
 * @author hudenian
 */
@Data
@ApiModel(value = "项目模型")
public class ProjectModelVo {

    @ApiModelProperty(value = "模型主键id")
    private String modelId;

    @ApiModelProperty(value = "模型所属的机构id")
    private String originId;

    @ApiModelProperty(value = "模型名称")
    private String fileName;

    @ApiModelProperty(value = "模型的元数据id")
    private String metadataId;

    @ApiModelProperty(value = "文件路径")
    private String filePath;

}
