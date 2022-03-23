package com.moirae.rosettaflow.vo.model;

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
public class ModelVo {

    @ApiModelProperty(value = "模型的元数据id")
    private String metaDataId;

    @ApiModelProperty(value = "模型所属的机构id")
    private String identityId;

    @ApiModelProperty(value = "模型名称")
    private String fileName;

}
