package com.datum.platform.vo.publicity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "公示信息")
public class PublicityVo {

    @ApiModelProperty(value = "图片url")
    private String imageUrl;

    @ApiModelProperty(value = "描述")
    private String describe;
}
