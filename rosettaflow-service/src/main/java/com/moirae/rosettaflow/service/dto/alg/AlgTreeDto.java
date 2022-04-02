package com.moirae.rosettaflow.service.dto.alg;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.moirae.rosettaflow.common.constants.SysConstant;
import com.moirae.rosettaflow.common.utils.LanguageContext;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
@ApiModel(value = "查询算法树详情响应参数")
public class AlgTreeDto {

    @ApiModelProperty(value = "算法或分类id")
    private Integer id;

    @ApiModelProperty(value = "算法或分类名称")
    private String name;

    @JsonIgnore
    private String nameEn;

    @ApiModelProperty(value = "算法或分类名称的图片url")
    private String imageUrl;

    @ApiModelProperty(value = "是否算法")
    private Boolean isAlgorithm;

    @ApiModelProperty(value = "是否存在对应算法")
    private Boolean isExistAlgorithm;

    @ApiModelProperty(value = "算法明细")
    private AlgDto alg;

    @ApiModelProperty(value = "明细")
    private List<AlgTreeDto> childrenList;

    public String getName(){
        return LanguageContext.getByLanguage(name, nameEn);
    }
}
