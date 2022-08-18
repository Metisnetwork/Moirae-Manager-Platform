package com.datum.platform.service.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 查询当前项目下面生成的所有模型列表
 *
 * @author hudenian
 */
@Data
@ApiModel
public class ModelDto {

    @ApiModelProperty(value = "模型的元数据id")
    private String metaDataId;

    @ApiModelProperty(value = "模型所属的机构id")
    private String identityId;

    @ApiModelProperty(value = "模型名称")
    private String name;
}
